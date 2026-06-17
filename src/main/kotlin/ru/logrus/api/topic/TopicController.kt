package ru.logrus.api.topic

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/internal/topics")
class TopicController(
    private val topicRepository: TopicRepository,
) {

    @GetMapping
    fun listTopics(): List<TopicResponse> =
        topicRepository.findAll()
}

data class TopicResponse(
    val id: UUID,
    val code: String,
    val title: String,
    val description: String?,
    val enabled: Boolean,
)
