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

## Local Database

The `local` profile expects PostgreSQL from the workspace Docker Compose infrastructure:

```text
jdbc:postgresql://localhost:5432/logrus
user: logrus
password: logrus
```

Liquibase changelog:

```text
classpath:db/changelog/db.changelog-master.yaml
```

On application startup, Liquibase creates the Phase 1 schema and required extensions (`pgcrypto`, `vector`) when available.

## Testcontainers

Database integration tests use `pgvector/pgvector:pg16` through Testcontainers.

For Docker Desktop on Windows with Docker Engine 29, test resources pin Docker Java API compatibility through:

```text
src/test/resources/docker-java.properties
src/test/resources/testcontainers.properties
```
