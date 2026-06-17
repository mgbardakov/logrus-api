package ru.logrus.api.source

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/internal/sources")
class SourceController(
    private val sourceRepository: SourceRepository,
) {

    @GetMapping
    fun listSources(): List<SourceResponse> =
        sourceRepository.findAll()
}

data class SourceResponse(
    val id: UUID,
    val code: String,
    val name: String,
    val sourceType: String,
    val priority: String,
    val enabled: Boolean,
)
