package ru.logrus.api.common

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@RestController
@RequestMapping("/api/internal/status")
class ApiStatusController {

    @GetMapping
    fun status(): ApiStatusResponse =
        ApiStatusResponse(
            service = "logrus-api",
            status = "UP",
            timestamp = OffsetDateTime.now(),
        )
}

data class ApiStatusResponse(
    val service: String,
    val status: String,
    val timestamp: OffsetDateTime,
)
