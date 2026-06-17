package ru.logrus.api.database

import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.sql.DriverManager

@Testcontainers
class LiquibaseMigrationTest {

    @Test
    fun `liquibase applies all migrations idempotently on clean database`() {
        DriverManager.getConnection(postgres.jdbcUrl, postgres.username, postgres.password).use { connection ->
            val database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(JdbcConnection(connection))
            val liquibase = Liquibase(
                "db/changelog/db.changelog-master.yaml",
                ClassLoaderResourceAccessor(),
                database,
            )

            liquibase.update(Contexts(), LabelExpression())
            liquibase.update(Contexts(), LabelExpression())

            val actualTables = connection.createStatement().use { statement ->
                statement.executeQuery(
                    """
                    select table_name
                    from information_schema.tables
                    where table_schema = 'public'
                      and table_type = 'BASE TABLE'
                      and table_name not in ('databasechangelog', 'databasechangeloglock')
                    order by table_name
                    """.trimIndent(),
                ).use { resultSet ->
                    buildSet {
                        while (resultSet.next()) {
                            add(resultSet.getString("table_name"))
                        }
                    }
                }
            }

            assertEquals(expectedTables, actualTables)
        }
    }

    class PgVectorContainer(
        imageName: DockerImageName,
    ) : PostgreSQLContainer<PgVectorContainer>(imageName)

    companion object {
        private val pgVectorImage = DockerImageName
            .parse("pgvector/pgvector:pg16")
            .asCompatibleSubstituteFor("postgres")

        @Container
        @JvmStatic
        val postgres = PgVectorContainer(pgVectorImage)
            .withDatabaseName("logrus")
            .withUsername("logrus")
            .withPassword("logrus")

        private val expectedTables = setOf(
            "analytical_hypotheses",
            "briefs",
            "documents",
            "entities",
            "event_entities",
            "event_topics",
            "events",
            "evidence",
            "raw_snapshots",
            "relation_topic_scores",
            "relations",
            "review_items",
            "source_runs",
            "sources",
            "topics",
            "users",
            "workspaces",
        )
    }
}
