# execute-configuration Specification

## Purpose

The `executeConfiguration` Maven goal runs Eclipse Epsilon transformation programs defined in external XML configuration files (validated against `epsilon_plugin_v1.xsd`). This allows transformation pipelines to be defined outside the POM, in reusable XML files that can be packaged as Maven artifacts or referenced via file wildcards.

## Architecture

The goal is implemented by `ExecuteEpsilonXmlConfigurationMojo`, which extends `AbstractMojo` directly (not `AbstractEpsilonMojo`). It parses configuration artifacts using JAXB, unmarshalling each into a `ConfigurationType` object. For each configuration file, it creates a fresh `ExecutionContext`, loads models, dispatches programs by type (ECL, EGL, EGX, EML, ETL, EVL, or plain EOL), executes them, and commits.

Key classes:
- `ExecuteEpsilonXmlConfigurationMojo` — The Mojo entry point (`@Mojo(name="executeConfiguration")`)
- JAXB-generated types from `epsilon_plugin_v1.xsd`: `ConfigurationType`, `EolType`, `EclType`, `EglType`, `EgxType`, `EmlType`, `EtlType`, `EvlType`
- Wrapper classes: `Eol`, `Ecl`, `Egl`, `Egx`, `Eml`, `Etl`, `Evl` (in `executeConfiguration` package) — bridge JAXB types to runtime contexts
- Model wrappers: `EmfModel`, `XmlModel`, `PlainXmlModel`, `ExcelModel` (in `executeConfiguration` package)
- `CompositeURIHandlerImpl` — Composes `MavenURIHandler` with `NioFilesystemnRelativePathURIHandlerImpl` for multi-scheme URI resolution

## Requirements

### Requirement: Configuration artifact resolution

The goal SHALL resolve configuration artifacts from Maven URIs (`mvn:*`) and file wildcards.

#### Scenario: Maven artifact configuration
- **GIVEN** `configurationArtifacts` contains `mvn:com.example:config:1.0:xml:epsilon`
- **WHEN** the `execute` goal runs
- **THEN** the `MavenURIHandler` resolves the artifact and it is added to the configuration URI list

#### Scenario: Wildcard file pattern
- **GIVEN** `configurationArtifacts` contains `src/main/epsilon/*.xml`
- **WHEN** the `execute` goal runs
- **THEN** `PlexusIoFileResourceCollection` resolves matching files relative to `sourceDirectory`

#### Scenario: Mixed sources
- **GIVEN** both Maven URIs and wildcard patterns are provided
- **WHEN** the `execute` goal runs
- **THEN** Maven URIs are resolved first, then wildcard patterns are expanded, and all are executed in order

### Requirement: XML configuration parsing

The goal SHALL unmarshal each configuration file using JAXB into a `ConfigurationType` instance.

#### Scenario: Valid XML configuration
- **GIVEN** a configuration XML file conforming to `epsilon_plugin_v1.xsd`
- **WHEN** the file is processed
- **THEN** JAXB unmarshals it into a `ConfigurationType` with metamodels, models, programs, and optional settings

#### Scenario: Configuration file not found
- **GIVEN** a resolved URI points to a non-existent file
- **WHEN** the goal checks `uriHandler.exists(uri)`
- **THEN** a `MojoFailureException` is thrown with message "Configuration file not found: <uri>"

### Requirement: Program type dispatch

The goal SHALL dispatch each program in the configuration to the correct Epsilon language executor based on its JAXB type.

#### Scenario: ETL program in configuration
- **GIVEN** the configuration contains an `<etl>` element
- **WHEN** programs are iterated via `getEclOrEglOrEgx()`
- **THEN** the program is detected as `EtlType` and wrapped with `Etl.builder().etl(...)` to produce an `EtlExecutionContext`

#### Scenario: Plain EOL program
- **GIVEN** the configuration contains a program that is not ECL, EGL, EGX, EML, ETL, or EVL
- **WHEN** the program is dispatched
- **THEN** it falls through to the default `Eol.builder().eol(...)` handling

### Requirement: Independent execution contexts per configuration

The goal SHALL create a fresh `ExecutionContext` and `CachedResourceSet` for each configuration file.

#### Scenario: Multiple configuration files
- **GIVEN** three configuration files are resolved
- **WHEN** the goal processes them
- **THEN** each gets its own `ResourceSet` and `ExecutionContext`, and failures in one propagate as `MojoExecutionException` with a log of previously completed configurations

### Requirement: Composite URI handling

The goal SHALL support `mvn:`, `file:`, and relative path URIs for model and metamodel references within configurations.

#### Scenario: File URI in model reference
- **GIVEN** a model in the XML configuration uses `file:///path/to/model.xmi`
- **WHEN** the model is loaded
- **THEN** `NioFilesystemnRelativePathURIHandlerImpl` with scheme `file` handles the resolution

### Requirement: Thread safety

The `execute()` method SHALL be synchronized to prevent concurrent execution.

#### Scenario: Concurrent Mojo invocation
- **WHEN** two threads attempt to call `execute()` simultaneously
- **THEN** the second call blocks until the first completes
