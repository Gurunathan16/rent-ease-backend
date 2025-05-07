package com.rentease.rental_management.rent.dto;

import jakarta.validation.constraints.NotNull;

public record PropertyInfo(

        @NotNull(message = "Id cannot be null.")
        Integer id
) { }
