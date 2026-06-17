package ru.logrus.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LogrusApiApplication

fun main(args: Array<String>) {
    runApplication<LogrusApiApplication>(*args)
}
