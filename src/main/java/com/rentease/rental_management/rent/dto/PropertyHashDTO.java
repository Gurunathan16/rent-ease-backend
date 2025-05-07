package com.rentease.rental_management.rent.dto;

public record PropertyHashDTO(
        String fullAddress,

        String subLocality,

        String locality,

        String city,

        String state,

        Integer pinCode,

        String listingCategory,

        String propertyType,

        String propertyCategory,

        String possessionStatus
) { }
