package com.rentease.rental_management.auth.service;

import com.rentease.rental_management.auth.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
//import sendinblue.ApiException;

import java.util.List;
import java.util.Map;

public interface UsersService
{
    ResponseEntity<Map<String, Object>> login(UsersLogin usersLogin);

    ResponseEntity<Map<String, Object>> register(UsersRegistration usersRegistration);

    ResponseEntity<Map<String, Object>> update(@Valid UsersUpdate usersUpdate);

    ResponseEntity<Map<String, Object>> updatePassword(UsersPasswordChange usersPasswordChange);

    ResponseEntity<Map<String, Object>> viewProfile();

    ResponseEntity<Map<String, Object>> delete();

    ResponseEntity<Map<String, Object>> refreshTokens(TokenRequest tokenRequest);

    ResponseEntity<Map<String, Object>> responseCookieGenerator(List<String> generatedTokens);

    ResponseEntity<Map<String, Object>> verifyEmailOTP(VerifyEmailOTPRequest verifyEmailOTPRequest);

    ResponseEntity<Map<String, Object>> sendOTP(String email);
}
