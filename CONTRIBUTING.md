# Contributing to epsilon-maven-plugin

This guide covers everything you need to get started contributing to the epsilon-maven-plugin, a Maven plugin that runs Eclipse Epsilon model transformation scripts from Maven builds.

## Development Environment Setup

### Required Tools

| Tool | Version | Download |
|------|---------|----------|
| JDK | 11 (Zulu JDK recommended) | [Azul Zulu JDK 11](https://www.azul.com/downloads/?version=java-11-lts&package=jdk) |
| Maven | 3.8.x | [Apache Maven](https://maven.apache.org/download.cgi) |

> **Note:** The project includes a Maven wrapper (`./mvnw`), so a system Maven installation is optional.

### Verifying Your Setup

Check Java version — you should see JDK 11:

```sh
java -version
# Expected output similar to:
# openjdk version "11.0.14" 2022-01-18 LTS
# OpenJDK Runtime Environment Zulu11.54+23-CA (build 11.0.14+9-LTS)
# OpenJDK 64-Bit Server VM Zulu11.54+23-CA (build 11.0.14+9-LTS, mixed mode)
```

Check Maven version:

```sh
mvn -version
# Expected: Apache Maven 3.8.x
```

## Project Architecture

This is a single-module Maven plugin project (no sub-modules). It provides three Maven goals for running Epsilon transformation scripts and parsing HUTN files.

### Component Overview

```mermaid
classDiagram
    class AbstractEpsilonMojo {
        +MavenProject project
        +RepositorySystem repoSystem
        +List~EmfModel~ emfModels
        +List~XmlModel~ xmlModels
        +List~PlainXmlModel~ plainXmlModels
        +List~ExcelModel~ excelModels
        +List~String~ metaModels
    }
    class ExecuteEpsilonMojo {
        +List~Eol~ eolPrograms
        +Boolean profile
        +Boolean addUmlPackages
        +execute()
    }
    class ParseHutnMojo {
        +File hutnFile
        +File targetFile
        +execute()
    }
    class ExecuteEpsilonXmlConfigurationMojo {
        +String[] configurationArtifacts
        +execute()
    }
    class MavenURIHandler {
        +canHandle(URI) boolean
        +createInputStream(URI) InputStream
        +getArtifactFile(URI) URI
    }
    class MavenLog {
        +info() void
        +warn() void
        +error() void
    }

    AbstractEpsilonMojo <|-- ExecuteEpsilonMojo
    AbstractEpsilonMojo <|-- ParseHutnMojo
    ExecuteEpsilonMojo --> MavenURIHandler
    ExecuteEpsilonMojo --> MavenLog
    ExecuteEpsilonXmlConfigurationMojo --> MavenURIHandler
    ExecuteEpsilonXmlConfigurationMojo --> MavenLog
```

### Execution Flow

The core execution pattern is shared by all goals — build an `ExecutionContext`, load models, run programs, then commit results:

```mermaid
sequenceDiagram
    participant Maven
    participant Mojo as ExecuteEpsilonMojo
    participant URIHandler as MavenURIHandler
    participant Context as ExecutionContext
    participant Epsilon as Epsilon Runtime

    Maven->>Mojo: execute()
    Mojo->>URIHandler: build (repoSystem, repositories)
    Mojo->>Context: executionContextBuilder()
    Note over Mojo,Context: Configure metamodels, models, profiling
    Mojo->>Context: load()
    loop For each Eol program
        Mojo->>Context: executeProgram(eolContext)
        Context->>Epsilon: run transformation
        Epsilon-->>Context: results
    end
    Mojo->>Context: commit()
    Context-->>Maven: done (or MojoExecutionException)
```

### Dependency Graph

```mermaid
graph LR
    subgraph External Dependencies
        EpsilonCore[Eclipse Epsilon Core 1.5.1]
        EpsilonEMF[Eclipse Epsilon EMF 1.5.1]
        EpsilonHUTN[Eclipse Epsilon HUTN 1.5.1]
        EMF[Eclipse EMF Ecore XMI]
        MavenAPI[Maven Plugin API 3.6.3]
        Aether[Maven Aether]
        JAXB[Jakarta XML Bind]
    end
    subgraph BlackBelt Runtime
        RuntimeUtils[epsilon-runtime-utils]
        RuntimeExec[epsilon-runtime-execution]
    end
    subgraph Plugin
        Execute[execute goal]
        ExecConfig[executeConfiguration goal]
        ParseHUTN[parseHutn goal]
    end
    Execute --> RuntimeExec
    Execute --> EpsilonCore
    Execute --> EMF
    ExecConfig --> RuntimeExec
    ExecConfig --> JAXB
    ParseHUTN --> EpsilonHUTN
    RuntimeExec --> EpsilonEMF
    Execute --> MavenAPI
    Execute --> Aether
```

## Build Commands

```sh
# Run tests
mvn clean test

# Full build and install to local repo
mvn clean install

# Full build with JaCoCo code coverage report
mvn clean verify
```

## Submission Guidelines

### Submitting an Issue

Before filing a new issue, search the [issue tracker](https://github.com/BlackBeltTechnology/epsilon-maven-plugin/issues) — your problem may already be reported or resolved.

When filing a bug report, include:

- Output of `java -version` and `mvn -version`
- Your `pom.xml` or `.flattened-pom.xml` (if applicable)
- A minimal reproduction case that demonstrates the failure

A minimal reproduction is essential — it lets maintainers quickly confirm the bug and ensures the right problem gets fixed.

File new issues via the [issue form](https://github.com/BlackBeltTechnology/epsilon-maven-plugin/issues/new/choose).

### Submitting a Pull Request

This project uses [GitHub's standard forking model](https://guides.github.com/activities/forking/). Fork the repository, create a feature branch, and submit a pull request.

For details on the CI/CD pipeline and branch conventions, see the [CI Flow](CIFLOW.md) documentation.
