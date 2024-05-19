package com.example.blog.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    val log = KotlinLogging.logger{}

    @ExceptionHandler(MethodArgumentResolutionException::class)
    fun handleMethodArgumentNotValidException(e:MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.error { "handleMethodArgmentNotValidException $e" }

        val of = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE,e.bindingResult)

        return ResponseEntity(of,HttpStatus.BAD_REQUEST)
    }

}