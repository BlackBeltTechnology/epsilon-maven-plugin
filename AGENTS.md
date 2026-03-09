# epsilon-maven-plugin - Project Documentation

## Project Overview


**Repository:** BlackBeltTechnology/epsilon-maven-plugin
**License:** Eclipse Public License 2.0 (EPL-2.0)
**Java Version:** 11
**Build System:** Maven 3.8.x with Maven Wrapper (./mvnw)

1. A Maven plugin that integrates Eclipse Epsilon model transformation scripts into Maven builds
2. Provides three Maven goals: `execute` (POM-configured transformations), `executeConfiguration` (XML-file-configured transformations), and `parseHutn` (HUTN-to-EMF model parsing)
3. Supports multiple model types: EMF models, XML models, plain XML models, and Excel spreadsheets
4. Resolves Maven artifacts as model/metamodel resources via a custom `mvn:` URI scheme handler
5. Part of the BlackBelt/JUDO ecosystem, used to run Epsilon EOL/ETL/EML/ECL/EVL/EGL/EGX scripts during the `generate-resources` build phase

## Code Instructions

1. First think through the problem, read the codebase for relevant files.
2. Before you make any major changes, check in with me and I will verify the plan.
3. Please every step of the way just give me a high level explanation of what changes you made.
4. Make every task and code change you do as simple as possible. We want to avoid making any massive or complex changes. Every change should impact as little code as possible. Everything is about simplicity.
5. Maintain a documentation file that describes how the architecture of the app works inside and out.
6. Never speculate about code you have not opened. If the user references a specific file, you MUST read the file before answering. Make sure to investigate and read relevant files BEFORE answering questions about the codebase. Never make any claims about code before investigating unless you are certain of the correct answer - give grounded and hallucination-free answers.
7. For implementation use TDD (Test-Driven Development): write or update tests first to define the expected behaviour, verify they fail, then write the minimal implementation to make them pass.
8. Use DRY (Don't Repeat Yourself): extract reusable logic into separate classes, utilities, or components. If the same pattern appears in multiple places, refactor it into a shared helper.

## Directory Structure

```
epsilon-maven-plugin/
├── pom.xml                          # Single-module Maven plugin POM
├── .mvn/                            # Maven wrapper config (Xms1024m, Xmx2048m)
├── src/
│   └── main/
│       ├── java/hu/blackbelt/epsilon/maven/plugin/
│       │   ├── execute/             # Direct POM-configured execution goal
│       │   ├── executeConfiguration/# XML config-file-based execution goal
│       │   ├── parsehutn/           # HUTN parsing goal
│       │   ├── MavenLog.java        # SLF4J Logger adapter for Maven logging
│       │   └── MavenURIHandler.java # mvn:* URI scheme resolver via Aether
│       ├── bindings/                # JAXB binding customizations
│       └── resources/
│           └── epsilon_plugin_v1.xsd# XML Schema for configuration files
├── .github/
│   └── workflows/                   # CI/CD: build, release, merge automation
├── CONTRIBUTING.md                  # Contribution guide
├── RELEASE-NOTES.md                 # Version changelog
└── logback-test.xml                 # Test logging configuration
```

## Core Modules

This is a single-module project. The source code is organized into three functional packages:

### Maven Goals

| Package | Goal Name | Class | Purpose |
|---------|-----------|-------|---------|
| `execute/` | `epsilon:execute` | `ExecuteEpsilonMojo` | Execute Epsilon programs configured directly in the POM |
| `executeConfiguration/` | `epsilon:executeConfiguration` | `ExecuteEpsilonXmlConfigurationMojo` | Execute Epsilon programs from external XML config files |
| `parsehutn/` | `epsilon:parseHutn` | `ParseHutnMojo` | Parse HUTN files into EMF models |

### Shared Infrastructure

| Class | Purpose |
|-------|---------|
| `AbstractEpsilonMojo` | Base Mojo with shared parameters: model lists, metamodels, Maven repository system |
| `MavenURIHandler` | Resolves `mvn:groupId:artifactId:version` URIs to local files via Aether |
| `MavenLog` | Bridges Maven's logging system to SLF4J for the Epsilon runtime |

### Model Types

| Class | Purpose |
|-------|---------|
| `EmfModel` | EMF model configuration with validation, caching, URI mapping |
| `XmlModel` | XML model with XSD schema support |
| `PlainXmlModel` | Plain XML model without schema |
| `ExcelModel` | Excel spreadsheet model |

All model classes are Lombok `@Data` POJOs with Maven `@Parameter` annotations and a `toModelContext()` method that converts to the runtime representation.

### Epsilon Program Types

| Class | Language | Purpose |
|-------|----------|---------|
| `Eol` | EOL | Epsilon Object Language — general-purpose scripting |
| `Etl` | ETL | Model-to-model transformation |
| `Eml` | EML | Model merging |
| `Ecl` | ECL | Model comparison |
| `Evl` | EVL | Model validation |
| `Egl` | EGL | Template-based code generation |
| `Egx` | EGX | EGL coordination (rule-based generation) |

## Technology Stack

### Core Technologies
- **Eclipse Epsilon 1.5.1** — Model management language framework (EOL, ETL, EML, ECL, EVL, EGL, EGX)
- **hu.blackbelt.epsilon:epsilon-runtime-execution** — BlackBelt's Epsilon runtime abstraction (ExecutionContext builder pattern)
- **Eclipse EMF (Ecore XMI 2.12/2.16)** — Eclipse Modeling Framework for model representation
- **Apache Maven Plugin API 3.6.3** — Maven plugin development framework
- **Maven Aether** — Artifact resolution for `mvn:` URI scheme
- **JAXB (Jakarta XML Bind 2.3.3)** — XML binding for configuration file parsing

### Utilities
- **Lombok 1.18.34** — Boilerplate reduction (`@Data`, `@Builder`)
- **Guava** — Collection utilities (Lists, Maps, ImmutableList)
- **SysOutOverSLF4J** — Redirects System.out/err to SLF4J

### Build & Quality
- **Maven Wrapper** — Bundled Maven distribution
- **maven-plugin-plugin 3.8.2** — Plugin descriptor and HelpMojo generation
- **jaxb2-maven-plugin 2.4** — Code generation from `epsilon_plugin_v1.xsd`
- **JaCoCo 0.8.12** — Code coverage
- **Surefire 3.5.1** — Test runner with `--add-opens` for Java module system
- **flatten-maven-plugin 1.2.7** — CI-friendly `${revision}` version resolution

## Build Commands

```bash
# Full build and install to local repository
./mvnw clean install

# Run tests only
./mvnw clean test

# Build with JaCoCo code coverage report
./mvnw clean verify

# Debug logging
./mvnw -X clean verify
```

> **Note:** JVM is configured with `-Xms1024m -Xmx2048m` via `.mvn/jvm.config`.

### Maven Profiles

| Profile | Purpose |
|---------|---------|
| `modules` | Active by default unless `-DskipModules=true` |
| `sign-artifacts` | Sign artifacts using `sign-maven-plugin` |
| `release-central` | Deploy to Maven Central via Sonatype OSSRH |
| `release-judong` | Deploy to Judo Technology Nexus |
| `generate-github-asciidoc-diagrams` | Generate documentation diagrams with AsciidoctorJ |
| `update-source-code-license` | Update EPL-2.0 license headers in source files |

## Key Configuration Files

| File | Purpose |
|------|---------|
| `pom.xml` | Single-module Maven POM with `${revision}` CI-friendly versioning |
| `.mvn/jvm.config` | JVM memory settings: `-Xms1024m -Xmx2048m` |
| `.mvn/wrapper/` | Maven wrapper distribution |
| `src/main/resources/epsilon_plugin_v1.xsd` | XML Schema for `executeConfiguration` goal's config files |
| `src/main/bindings/SkipElementProperties.xjc` | JAXB binding customizations for XSD code generation |
| `logback-test.xml` | SLF4J/Logback logging config for test execution |

## Development Environment

**Required:**
- Java 11 JDK (Zulu JDK recommended)
- Maven 3.8.x (or use bundled `./mvnw`)

**Build generates:**
- JAXB classes from `epsilon_plugin_v1.xsd` during `generate-resources` phase
- Delombok'd sources in `target/delombok` for Javadoc generation
- Plugin descriptor in `META-INF/maven/plugin.xml`

## Git Workflow

- **Main Branch:** `develop`
- **Release Branch:** `master` (contains latest stable release)
- **Versioning:** `${revision}` property (currently `1.2.1-SNAPSHOT`), resolved by `flatten-maven-plugin`
- **Branch naming:** `feature/JNG-xxx_summary`, `bugfix/JNG-xxx_summary`, `release/X.Y.Z`
- **Rule:** Every commit must reference a JIRA ticket (`JNG-xxx`)
- **CI/CD:** GitHub Actions — automated build, release PR creation, Nexus deployment

## Important Notes

1. All three Mojo `execute()` methods are `synchronized` for thread safety
2. The `executeConfiguration` goal uses JAXB to unmarshal XML config files against `epsilon_plugin_v1.xsd` — JAXB classes are generated at build time and won't exist until after `mvn generate-resources`
3. The `MavenURIHandler` resolves `mvn:groupId:artifactId:version` URIs by delegating to Maven Aether for artifact resolution, then using the local filesystem for I/O
4. All Epsilon program wrappers (Eol, Etl, etc.) and model types follow the same pattern: Maven `@Parameter`-annotated POJOs with a `toExecutionContext()` or `toModelContext()` conversion method
5. The `executeConfiguration` package mirrors the `execute` package structure but wraps JAXB-generated types instead of Maven parameter types
6. Source files carry EPL-2.0 license headers managed by the `update-source-code-license` Maven profile
7. No unit tests currently exist in `src/test/`, though Surefire and JaCoCo are fully configured
8. The Epsilon runtime dependency uses a custom BlackBelt build version (`2.8.0.20251022_112123_14c440b1_develop`), not a standard Maven Central release

## Related Documentation

- [CONTRIBUTING.md](CONTRIBUTING.md) — Development setup, architecture overview, submission guidelines
- [.github/CIFLOW.md](.github/CIFLOW.md) — CI/CD pipeline details, branching strategy, versioning policy
- [RELEASE-NOTES.md](RELEASE-NOTES.md) — Version changelog
