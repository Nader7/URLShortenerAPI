package org.origin.urlshortenerapi.exception;

import org.origin.urlshortenerapi.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    private ResponseEntity<ExceptionResponse> handleResponseStatusException(ResponseStatusException e) {
        ExceptionResponse exceptionResponse = new  ExceptionResponse(
                LocalDateTime.now(),
                e.getStatusCode().value(),
                HttpStatus.valueOf(e.getStatusCode().value()).getReasonPhrase(),
                e.getReason());

        return ResponseEntity.status(e.getStatusCode().value()).body(exceptionResponse);
    }
}
