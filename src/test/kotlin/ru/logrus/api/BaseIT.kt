package ru.logrus.api

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.sql.Connection
import java.sql.DriverManager

@Testcontainers
abstract class BaseIT {

    protected fun getConnection(): Connection =
        DriverManager.getConnection(postgres.jdbcUrl, postgres.username, postgres.password)

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

        @DynamicPropertySource
        @JvmStatic
        fun datasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
            registry.add("spring.liquibase.enabled") { "true" }
        }
    }
}
