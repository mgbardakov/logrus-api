package ru.logrus.api.database

import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.logrus.api.BaseIT

class LiquibaseMigrationIT : BaseIT() {

    @Test
    fun `liquibase applies all migrations idempotently on clean database`() {
        getConnection().use { connection ->
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

    companion object {
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
