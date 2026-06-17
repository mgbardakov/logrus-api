package ru.logrus.api.source

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID

@Repository
class SourceRepository(
    private val jdbcClient: JdbcClient,
) {

    fun findAll(): List<SourceResponse> =
        jdbcClient.sql(
            """
            select id, code, name, source_type, priority, enabled
            from sources
            order by code
            """.trimIndent(),
        )
            .query(::mapSource)
            .list()

    private fun mapSource(rs: ResultSet, rowNum: Int): SourceResponse =
        SourceResponse(
            id = rs.getObject("id", UUID::class.java),
            code = rs.getString("code"),
            name = rs.getString("name"),
            sourceType = rs.getString("source_type"),
            priority = rs.getString("priority"),
            enabled = rs.getBoolean("enabled"),
        )
}
