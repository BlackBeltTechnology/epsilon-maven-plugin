# execute Specification

## Purpose

The `execute` Maven goal runs Eclipse Epsilon transformation programs (EOL, ETL, EML, ECL, EVL, EGL, EGX) against configured models during the `generate-resources` phase. Programs and models are configured directly in the Maven POM.

## Architecture

The goal is implemented by `ExecuteEpsilonMojo`, which extends `AbstractEpsilonMojo`. The base class provides shared parameters for model lists (EMF, XML, PlainXML, Excel), metamodels, and Maven repository access. The Mojo collects all model configurations, converts them to runtime model contexts via `toModelContext()`, builds an `ExecutionContext` using the builder pattern, loads models, iterates over configured `eolPrograms`, executes each one, and commits results.

Key classes:
- `ExecuteEpsilonMojo` — The Mojo entry point (`@Mojo(name="execute")`)
- `AbstractEpsilonMojo` — Base class with shared `@Parameter` fields
- `Eol`, `Etl`, `Eml`, `Ecl`, `Evl`, `Egl`, `Egx` — Program type wrappers, each with `toExecutionContext()`
- `EmfModel`, `XmlModel`, `PlainXmlModel`, `ExcelModel` — Model configuration POJOs with `toModelContext()`
- `MavenURIHandler` — Resolves `mvn:` URIs to local artifact files
- `MavenLog` — Bridges Maven logging to SLF4J
- `InjectedContext` — Injects custom Java objects into the Epsilon execution scope
- `ProfilingExecutionListener` — Optional execution profiling

## Requirements

### Requirement: Model collection and conversion

The goal SHALL collect all configured models (EMF, XML, PlainXML, Excel) and convert each to a runtime model context.

#### Scenario: Multiple model types configured
- **GIVEN** the POM configures `emfModels`, `xmlModels`, and `plainXmlModels`
- **WHEN** the `execute` goal runs
- **THEN** all models from all three lists are converted via `toModelContext()` and passed to the `ExecutionContext`

#### Scenario: No models of a given type
- **GIVEN** the POM does not configure `excelModels` (the list is null)
- **WHEN** the `execute` goal runs
- **THEN** the null list is skipped without error

### Requirement: Epsilon program execution

The goal SHALL execute each program in `eolPrograms` in order against the loaded models.

#### Scenario: Sequential program execution
- **GIVEN** three EOL programs are configured in `eolPrograms`
- **WHEN** the `execute` goal runs
- **THEN** each program's `toExecutionContext()` is called and passed to `executionContext.executeProgram()` in declaration order

#### Scenario: ETL with transformation trace export
- **GIVEN** an ETL program is configured with `exportTransformationTrace` set to a file path
- **WHEN** the ETL program executes
- **THEN** the transformation trace is exported to the specified path

### Requirement: Maven artifact URI resolution

The goal SHALL resolve `mvn:groupId:artifactId:version` URIs in model and metamodel references.

#### Scenario: Model references a Maven artifact
- **GIVEN** an EMF model's `emf` parameter uses a `mvn:` URI
- **WHEN** the `ExecutionContext` loads the model
- **THEN** `MavenURIHandler` resolves the artifact via Aether and provides a file-based `InputStream`

### Requirement: Context injection

The goal SHALL support injecting custom Java objects into the Epsilon execution context.

#### Scenario: Injected context configured
- **GIVEN** `injectedContexts` contains an entry with `name=myHelper` and `clazz=com.example.Helper`
- **WHEN** the `execute` goal runs
- **THEN** an instance of `com.example.Helper` is created via `Class.forName().newInstance()` and added to the context map under key `myHelper`

#### Scenario: Injected context class not found
- **GIVEN** `injectedContexts` contains an entry with an invalid class name
- **WHEN** the `execute` goal runs
- **THEN** a warning is logged and execution continues

### Requirement: Thread safety

The `execute()` method SHALL be synchronized to prevent concurrent execution.

#### Scenario: Concurrent Mojo invocation
- **WHEN** two threads attempt to call `execute()` simultaneously
- **THEN** the second call blocks until the first completes

### Requirement: Resource cleanup

The goal SHALL use try-with-resources to ensure the `ExecutionContext` is properly closed after execution.

#### Scenario: Execution completes normally
- **GIVEN** all programs execute successfully
- **WHEN** execution finishes
- **THEN** `executionContext.commit()` is called, then the context is closed

#### Scenario: Execution fails with exception
- **GIVEN** a program throws an exception during execution
- **WHEN** the exception propagates
- **THEN** the `ExecutionContext` is still closed and a `MojoExecutionException` is thrown

### Requirement: Profiling support

The goal SHALL optionally enable profiling when the `profile` parameter is `true`.

#### Scenario: Profiling enabled
- **GIVEN** `profile` is set to `true` in the POM
- **WHEN** the `ExecutionContext` is built
- **THEN** profiling is enabled via `executionContextBuilder().profile(true)`

### Requirement: Metamodel package registration

The goal SHALL optionally register UML and Ecore metamodel packages.

#### Scenario: UML packages requested
- **GIVEN** `addUmlPackages` is set to `true`
- **WHEN** the `ExecutionContext` is built
- **THEN** UML metamodel packages are registered via `executionContextBuilder().addUmlPackages(true)`
