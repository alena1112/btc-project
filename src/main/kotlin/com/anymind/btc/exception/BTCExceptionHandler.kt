package com.anymind.btc.exception

import com.anymind.btc.resources.dto.RestResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class BTCExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(BTCException::class)
    fun handleLMException(e: BTCException): ResponseEntity<RestResponse> {
        log.error("Error processing request: {}", e)
        val body = RestResponse("Exception: " + e.message)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(e: Throwable): ResponseEntity<InternalError> {
        log.error("Error processing request: {}", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(InternalError(e))
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(BTCExceptionHandler::class.java)
    }
}