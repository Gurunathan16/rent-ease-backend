package com.rentease.rental_management.rent.dto;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public record PropertyFilterDTO (
        Set<String> propertyType,
        Set<Integer> bhkType,
        Integer minPrice,
        Integer maxPrice,
        Integer availableWithin,
        Set<String> preferredTenants,
        Set<String> furnishedStatus,
        Set<String> parking,
        String listingCategory,
        Integer propertyAge,
        Integer bathrooms,
        Set<Integer> amenities,
        Integer minArea,
        Integer maxArea
) { }
