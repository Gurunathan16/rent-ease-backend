package com.rentease.rental_management.util.exception;

import com.rentease.rental_management.util.response.ResponseEntityHandler;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request from Client!", "Error", ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandler(NoHandlerFoundException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "Trying to hit non-existing Endpoint 🫠", "Error", ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, "Check the HTTP Method!", "Error",
                ex.getMessage());
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Map<String, Object>> handlePropertyReference(PropertyReferenceException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request. Check the attributes.", "Error", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Illegal Argument has been sent to the backend.", "Error", ex.getMessage());
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<Map<String, Object>> handleTooManyRequests(TooManyRequestsException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(),
                "Recovery", "Try after few minutes.");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEntry(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Duplicate Entry found.", "Recovery", "Try adding different entry. Not the same one 🙃.");
    }
}
