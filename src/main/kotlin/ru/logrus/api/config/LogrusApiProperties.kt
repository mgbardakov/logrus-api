package ru.logrus.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "logrus.api")
data class LogrusApiProperties(
    val serviceName: String = "logrus-api",
)
