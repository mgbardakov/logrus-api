package ru.logrus.api.topic

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID

@Repository
class TopicRepository(
    private val jdbcClient: JdbcClient,
) {

    fun findAll(): List<TopicResponse> =
        jdbcClient.sql(
            """
            select id, code, title, description, enabled
            from topics
            order by code
            """.trimIndent(),
        )
            .query(::mapTopic)
            .list()

    private fun mapTopic(rs: ResultSet, rowNum: Int): TopicResponse =
        TopicResponse(
            id = rs.getObject("id", UUID::class.java),
            code = rs.getString("code"),
            title = rs.getString("title"),
            description = rs.getString("description"),
            enabled = rs.getBoolean("enabled"),
        )
}
