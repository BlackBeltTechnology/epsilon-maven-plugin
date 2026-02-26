# CI/CD Flow: Development Versioning and Branch Handling

This document describes the branching strategy, versioning policy, and GitHub Actions CI/CD pipeline used by this project. The workflow is based on GitFlow, adapted for automated releases via GitHub Actions.

## Branching Strategy

The project follows a [GitFlow-based workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) with five branch types:

| Branch Pattern | Base Branch | Purpose |
|---------------|-------------|---------|
| `develop` | — | Main development branch; contains latest sources of the active version |
| `feature/JNG-NUMBER_summary` | `develop` | New feature work; merged back to `develop` when complete |
| `release/X.Y.Z` or `X_Y_Z` | `develop` | Release stabilization; `release/` prefix is reserved for CI |
| `bugfix/JNG-NUMBER_summary` | release branch | Bug fixes during release testing; applied to release and newer develop |
| `support/JNG-NUMBER_summary` | release branch | Minor changes to a previous release |
| `hotfix/JNG-NUMBER_summary` | `master` | Critical fixes applied to both `master` and release branches |
| `master` | — | Contains the latest released (stable) sources |

### Branch Lifecycle

```mermaid
gitGraph
    commit id: "init"
    branch develop
    commit id: "dev-1"
    branch feature/JNG-1
    commit id: "feat-1"
    commit id: "feat-2"
    checkout develop
    merge feature/JNG-1 id: "merge-feat"
    branch release/1.0
    commit id: "stabilize"
    branch bugfix/JNG-4
    commit id: "fix"
    checkout release/1.0
    merge bugfix/JNG-4 id: "merge-fix"
    checkout main
    merge release/1.0 id: "release-1.0"
    checkout develop
    merge release/1.0 id: "back-merge"
    commit id: "dev-2"
```

## Version Numbers

Versions follow semantic versioning with these rules:

| Event | Version Change |
|-------|---------------|
| Start a `feature/` branch | No version change |
| Start a `release/` branch | Increment 2nd number on `develop` |
| Start a `bugfix/` branch | No change (applied to release during testing) |
| Start a `support/` branch | Increment 3rd number |
| Start a `hotfix/` branch | Increment 4th number |

## GitHub Actions Workflows

The CI/CD pipeline consists of four interconnected workflows that automate building, testing, deploying, and releasing.

### Pipeline Overview

```mermaid
flowchart TD
    subgraph Triggers
        Push[Push to develop]
        PR[PR to develop / master / release]
        Manual[Manual trigger with version]
        MasterPush[Push to master]
        TagPush[Push merge-pr/* tag]
    end

    subgraph Workflows
        Build[build.yml]
        MergePR[merge-pr-tagged.yml]
        CreateRelease[create-release-on-master.yml]
        Release[release.yml]
    end

    Push --> Build
    PR --> Build
    Manual --> Release
    MasterPush --> CreateRelease
    TagPush --> MergePR

    Build -->|"creates merge-pr/ tag<br>(for release/increment PRs)"| MergePR
    MergePR -->|"merges to master<br>(release versions)"| CreateRelease
    MergePR -->|"squashes to develop<br>(non-release versions)"| Build
    Release -->|"creates PRs to<br>master + develop"| Build
```

### build.yml

Triggered on pushes to `develop` or pull requests targeting `develop`, `master`, `increment/*`, or `release/*`.

```mermaid
flowchart LR
    Start([Push / PR]) --> BranchCheck{Branch type?}
    BranchCheck -->|master, release/*| StableVersion[Set version from pom.xml<br>without -SNAPSHOT]
    BranchCheck -->|develop, increment/*| DevVersion[Set version as<br>major.minor.qualifier.date_commitId_branch]
    StableVersion --> BuildDeploy[Build & deploy to Nexus]
    DevVersion --> BuildDeploy
    BuildDeploy --> Tag[Create git tag v-version]
    Tag --> IsMergeable{release/* or increment/*?}
    IsMergeable -->|Yes| MergeTag[Create merge-pr/version tag<br>Triggers merge-pr-tagged.yml]
    IsMergeable -->|No| IsDevelop{develop?}
    IsDevelop -->|Yes| GHRelease[Build changelog<br>Create GitHub prerelease]
    IsDevelop -->|No| Done([End])
    MergeTag --> Done
    GHRelease --> Done
```

### merge-pr-tagged.yml

Triggered when a `merge-pr/*` tag is pushed. Routes the merge based on version format:

```mermaid
flowchart LR
    Start([merge-pr/* tag pushed]) --> Extract[Extract version from tag]
    Extract --> VersionCheck{Version format?}
    VersionCheck -->|major.minor.qualifier| MergeMaster[Merge PR to master<br>Triggers create-release-on-master.yml]
    VersionCheck -->|other| SquashDevelop[Squash PR to develop<br>Triggers build.yml]
    MergeMaster --> Cleanup[Delete merge-pr/* tag]
    SquashDevelop --> Cleanup
```

### create-release-on-master.yml

Triggered on push to `master`. Builds a changelog and creates a GitHub release (marked as "latest").

### release.yml

Triggered manually with a version parameter (`'auto'` or `major.minor.qualifier`):

```mermaid
flowchart LR
    Start([Manual trigger]) --> VersionInput{Given version?}
    VersionInput -->|auto| FromPom[Use version from pom.xml<br>without -SNAPSHOT]
    VersionInput -->|specific| UseGiven[Use given version]
    FromPom --> CalcNext[Next version = qualifier + 1]
    UseGiven --> CalcNext
    CalcNext --> PRMaster[Create PR to master<br>with release version]
    CalcNext --> PRDevelop[Create PR to develop<br>with next version]
    PRMaster --> BuildTriggered([Triggers build.yml])
    PRDevelop --> BuildTriggered
```

## Development Rules

> **Important:** There is no commit without a ticket number. Every pull request and commit must include a JIRA ticket reference in the format `JNG-xxx`.

Issue tracking uses [JIRA](https://blackbelt.atlassian.net/jira/dashboards).
