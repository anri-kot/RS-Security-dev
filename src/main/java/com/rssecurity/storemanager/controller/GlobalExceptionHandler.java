package com.rssecurity.storemanager.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(HttpServletRequest request, ResourceNotFoundException ex) {
        return handleError(request, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(HttpServletRequest request, ConflictException ex) {
        return handleError(request, ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(HttpServletRequest request, BadRequestException ex) {
        return handleError(request, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        return handleError(request, "Ocorreu um erro inesperado.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> handleError(HttpServletRequest request, String message, HttpStatus status) {
        if (isHtmx(request)) {
            String html = "<div class='alert alert-danger alert-dismissible'>" + message + "</div>";
            return ResponseEntity.status(status).body(html);
        }

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);

        return new ResponseEntity<>(errorDetails, status);
    }

    private boolean isHtmx(HttpServletRequest request) {
        return "true".equalsIgnoreCase(request.getHeader("HX-Request"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        StringBuilder message = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if (!message.isEmpty()) message.append(", ");
            errors.put(error.getField(), error.getDefaultMessage());
            message.append(error.getField() + ": " + error.getDefaultMessage());
        });

        return handleError(request, message.toString(), HttpStatus.BAD_REQUEST);
    }
}
