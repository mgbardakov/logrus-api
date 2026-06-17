# logrus-api

Backend core for LOGRUS MVP.

## Role

`logrus-api` owns the core domain and internal API surface:

- sources and ingestion;
- raw snapshots and normalized documents;
- events, entities, topics, relations, evidence, hypotheses, and briefs;
- persistence integrations;
- background processing;
- LLM gateway boundary.

`logrus-api` is an independent Git repository and an independent Gradle build. It is not a Gradle submodule of `logrus-bff`.

## Stack

- Kotlin
- Spring Boot
- Spring MVC
- Gradle Kotlin DSL
- Java 21

## Package Convention

The root package is:

```text
ru.logrus.api
```

No shared module is introduced between `logrus-api` and `logrus-bff` during Phase 1. Contracts are kept at the HTTP/API boundary.

## Local Build

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

## Profiles

- `local` - local development defaults;
- `test` - automated test defaults.
