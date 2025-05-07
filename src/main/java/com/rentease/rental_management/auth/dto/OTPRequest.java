package com.rentease.rental_management.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OTPRequest(
        @NotBlank(message = "Email ID cannot be null or blank.")
        @Email
        String email
) { }
