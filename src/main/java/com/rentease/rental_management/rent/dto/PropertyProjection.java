package com.rentease.rental_management.rent.dto;

import com.rentease.rental_management.rent.entity.Address;
import com.rentease.rental_management.rent.entity.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyProjection
{
    private Integer id;

    private String title;

    private String description;

    private Address address;

    private Price price;

    private Integer bedrooms;

    private Integer bathrooms;

    private Boolean isAttachedBathroom;

    private Integer balconies;

    private Boolean isAttachedBalcony;

    private Integer propertyFloor;

    private Integer totalFloor;

    private String facing;

    private Boolean isMainRoadFacing;

    private Integer buildUpArea;

    private Integer carpetArea;

    private Integer propertyAge;

    private LocalDate availableFrom;

    private Integer noticePeriodInMonths;

    private Boolean gatedSecurity;

    private Boolean gym;

    private Boolean onlyVeg;

    private Set<String> amenities;

    private Set<String> preferredTenants;

    private String propertyCategory;

    private String waterSupply;

    private String listingCategory;

    private String propertyType;

    private String furnishedStatus;

    private String possessionStatus;

    private String parking;
}
