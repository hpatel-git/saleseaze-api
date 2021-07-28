package com.saleseaze.api.exception.handler

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.saleseaze.api.exception.InvalidDataException
import com.saleseaze.api.model.ApplicationError
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.Locale

@RestControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(value = [(InvalidDataException::class)])
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUserAlreadyExists(ex: InvalidDataException): ApplicationError {
        return ApplicationError(
            HttpStatus.BAD_REQUEST.value(), ex.message ?: "Validation error"
        )
    }

    @ExceptionHandler(value = [(MissingKotlinParameterException::class)])
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(ex: MissingKotlinParameterException):
            ApplicationError {
        return createMissingKotlinParameterViolation(ex)
    }

    private fun createMissingKotlinParameterViolation(
        ex: MissingKotlinParameterException
    ): ApplicationError {
        val error =
            ApplicationError(
                HttpStatus.BAD_REQUEST.value(), "validation error"
            )
        val errorFieldRegex = Regex("\\.([^.]*)\\[\\\"(.*)\"\\]\$")
        val errorMatch = errorFieldRegex.find(ex.path[0].description)!!
        val (objectName, field) = errorMatch.destructured
        error.addFieldError(
            objectName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
            field,
            "must not be null"
        )
        return error
    }

}