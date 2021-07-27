package com.saleseaze.api.exception.handler

import com.saleseaze.api.exception.InvalidUserException
import com.saleseaze.api.model.ApplicationError
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ApplicationExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(InvalidUserException::class)])
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUserAlreadyExists(ex: InvalidUserException): ApplicationError {
        return ApplicationError(
            HttpStatus.BAD_REQUEST.name, ex.message
        )
    }

}