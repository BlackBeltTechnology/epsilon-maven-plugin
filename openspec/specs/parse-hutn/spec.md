# parse-hutn Specification

## Purpose

The `parseHutn` Maven goal parses HUTN (Human-Usable Textual Notation) files into EMF models using the Eclipse Epsilon HUTN module. This allows human-readable model definitions to be converted into standard EMF XMI format during the build.

## Architecture

The goal is implemented by `ParseHutnMojo`, which extends `AbstractEpsilonMojo`. It creates an `ExecutionContext` with EMF and PlainXML models, instantiates a `HutnModule`, configures it with a `HutnContext` bound to the execution's `ModelRepository`, parses the HUTN file, and stores the resulting EMF model.

Key classes:
- `ParseHutnMojo` — The Mojo entry point (`@Mojo(name="parseHutn")`)
- `AbstractEpsilonMojo` — Provides `emfModels`, `plainXmlModels`, `metaModels`, and repository access
- `HutnModule` (Eclipse Epsilon) — Parses HUTN files
- `HutnContext` (Eclipse Epsilon) — Configures error/warning/output streams and model repository for HUTN parsing

## Requirements

### Requirement: HUTN file parsing

The goal SHALL parse the specified HUTN file using the Epsilon `HutnModule`.

#### Scenario: Valid HUTN file
- **GIVEN** `hutnFile` points to a syntactically valid HUTN file
- **WHEN** `module.parse(hutnFile)` is called
- **THEN** the HUTN content is parsed successfully and `parse()` returns `true`

#### Scenario: Invalid HUTN file
- **GIVEN** `hutnFile` points to a HUTN file with syntax errors
- **WHEN** `module.parse(hutnFile)` is called
- **THEN** `parse()` returns `false` and a `MojoExecutionException` is thrown containing all parse problems from `module.getParseProblems()`

### Requirement: EMF model output

The goal SHALL store the parsed HUTN content as an EMF model at the specified target location.

#### Scenario: Successful model storage
- **GIVEN** HUTN parsing succeeds
- **WHEN** `module.storeEmfModel()` is called
- **THEN** the EMF model is written to `targetFile`'s parent directory with `targetFile`'s name

### Requirement: Model repository integration

The goal SHALL configure the HUTN module's context with the `ExecutionContext`'s `ModelRepository`.

#### Scenario: Models available for HUTN resolution
- **GIVEN** `emfModels` and `plainXmlModels` are configured
- **WHEN** the `ExecutionContext` is built and loaded
- **THEN** the `HutnContext` is configured with `executionContext.getProjectModelRepository()`, making all loaded models available for reference resolution during HUTN parsing

### Requirement: Execution context lifecycle

The goal SHALL create an `ExecutionContext` with EMF and PlainXML models, commit after parsing, and close via try-with-resources.

#### Scenario: Normal execution
- **GIVEN** valid models and a valid HUTN file
- **WHEN** parsing and storage complete
- **THEN** `executionContext.commit()` is called and the context is closed

#### Scenario: Exception during parsing
- **GIVEN** an exception occurs during HUTN processing
- **WHEN** the exception propagates
- **THEN** the `ExecutionContext` is still closed (try-with-resources) and the error is logged

### Requirement: Thread safety

The `execute()` method SHALL be synchronized to prevent concurrent execution.

#### Scenario: Concurrent Mojo invocation
- **WHEN** two threads attempt to call `execute()` simultaneously
- **THEN** the second call blocks until the first completes
