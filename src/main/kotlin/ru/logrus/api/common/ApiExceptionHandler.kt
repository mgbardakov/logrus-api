package ru.logrus.api.common

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val message = exception.bindingResult.fieldErrors
            .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }
            .ifBlank { "Validation failed" }

        return error(HttpStatus.BAD_REQUEST, message, request)
    }

    @ExceptionHandler(
        ConstraintViolationException::class,
        HttpMessageNotReadableException::class,
        MethodArgumentTypeMismatchException::class,
    )
    fun handleBadRequest(
        exception: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> =
        error(HttpStatus.BAD_REQUEST, exception.message ?: "Bad request", request)

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(
        exception: NoSuchElementException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> =
        error(HttpStatus.NOT_FOUND, exception.message ?: "Resource not found", request)

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(
        exception: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> =
        error(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = exception.message ?: "Unexpected API error",
            request = request,
        )

    private fun error(
        status: HttpStatus,
        message: String,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> =
        ResponseEntity
            .status(status)
            .body(
                ApiErrorResponse(
                    status = status.value(),
                    error = status.reasonPhrase,
                    message = message,
                    path = request.requestURI,
                ),
            )
}
