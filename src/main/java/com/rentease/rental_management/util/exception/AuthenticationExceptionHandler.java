package com.rentease.rental_management.util.exception;

import com.rentease.rental_management.util.response.ResponseEntityHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AuthenticationExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Bad Credentials. " +
                "Authentication failed.","Validation Error", "Invalid username or password");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, Object>> handleDisabled(DisabledException ex)
    {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Account is disabled.",
                "Recovery", "Write email to our team stating your side views.");
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Map<String, Object>> handleLocked(LockedException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Account is locked.",
                "Recovery", "Verify Email OTP to unlock account.");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleExpired(AccountExpiredException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Account is Expired.",
                "Recovery", "Continue Reset Password. Verify Email then.");
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleCredentialsExpired(CredentialsExpiredException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Password Expired.",
                "Recovery", "Continue Reset Password by hitting /auth/changePassword.");
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceError(AuthenticationServiceException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication " +
                "service error.", "Recovery", "Try again after sometime.");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundError(AuthenticationServiceException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Username not found.",
                "Recovery", ex);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuth(AuthenticationException ex) {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Authentication failed.", "details", ex.getMessage());
    }
}
