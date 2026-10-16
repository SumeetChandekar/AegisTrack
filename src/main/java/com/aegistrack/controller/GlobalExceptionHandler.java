package com.aegistrack.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException exception) {

        String message = exception.getMessage();

        HttpStatus status = message != null
                && message.contains("not found")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        Map<String, String> body = Map.of(
                "error", status.getReasonPhrase(),
                "message", message != null
                        ? message
                        : "Invalid request."
        );

        return ResponseEntity.status(status).body(body);
    }
}