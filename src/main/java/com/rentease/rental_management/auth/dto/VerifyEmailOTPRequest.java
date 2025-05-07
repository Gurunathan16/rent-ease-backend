package com.rentease.rental_management.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyEmailOTPRequest(
        @NotBlank(message = "Email ID cannot be null or blank.")
        @Email
        String email,

        @NotBlank(message = "OTP cannot be null or blank.")
        @Pattern(regexp = "^\\d{6}$", message = "OTP should be exactly 6 characters")
        String otp

) {
}
