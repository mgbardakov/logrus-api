package ru.logrus.api.database

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest
class LogrusApiDatabaseApplicationTest {

    @Test
    fun `application context starts with liquibase enabled`() {
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
