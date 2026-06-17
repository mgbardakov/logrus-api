package ru.logrus.api.database

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import ru.logrus.api.BaseIT

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogrusApiDatabaseApplicationIT(
    @Autowired private val restTemplate: TestRestTemplate,
) : BaseIT() {

    @LocalServerPort
    private var port: Int = 0

    @Test
    fun `application context starts with liquibase enabled`() {
    }

    @Test
    fun `health endpoint returns up`() {
        val response = restTemplate.getForEntity(
            "http://localhost:$port/actuator/health",
            Map::class.java,
        )

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.get("status") == "UP")
    }

    @Test
    fun `minimal internal endpoints return empty lists on clean database`() {
        val topics = restTemplate.getForEntity(
            "http://localhost:$port/api/internal/topics",
            List::class.java,
        )
        val sources = restTemplate.getForEntity(
            "http://localhost:$port/api/internal/sources",
            List::class.java,
        )

        assert(topics.statusCode == HttpStatus.OK)
        assert(topics.body == emptyList<Any>())
        assert(sources.statusCode == HttpStatus.OK)
        assert(sources.body == emptyList<Any>())
    }
}
