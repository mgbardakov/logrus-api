package ru.logrus.api.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(LogrusApiProperties::class)
class LogrusApiConfiguration
