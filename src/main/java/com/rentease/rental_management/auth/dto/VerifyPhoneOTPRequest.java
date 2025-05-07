package com.rentease.rental_management.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VerifyPhoneOTPRequest(
        @NotBlank(message = "Phone number cannot be null or blank.")
        @Pattern(regexp = "^\\+\\d{12}$", message = "Phone number not in correct format.")
        String phoneNumber,

        @NotBlank(message = "OTP cannot be null or blank.")
        @Pattern(regexp = "^\\d{6}$")
        String otp
) { }
