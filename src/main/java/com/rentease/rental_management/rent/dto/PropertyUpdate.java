package com.rentease.rental_management.rent.dto;

import com.rentease.rental_management.rent.entity.Property;
import com.rentease.rental_management.util.annotations.ValidCarpetArea;
import com.rentease.rental_management.util.annotations.ValidPropertyFloor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@ValidPropertyFloor
@ValidCarpetArea
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyUpdate
{
    @NotNull(message = "Property Id cannot be null.")
    private Integer id;

    @NotBlank(message = "Title cannot be null or blank.")
    @Size(min = 10, max = 25, message = "Title cannot be less than 10 characters and more than 25 characters.")
    private String title;

    @NotBlank(message = "Description cannot be null or blank.")
    @Size(min = 30, max = 5000, message = "Description cannot be less than 30 characters and more than 5000 " +
        "characters.")
    private String description;

    @NotNull(message = "Address cannot be null.")
    private AddressDTO address;

    @NotNull(message = "Price Details cannot be null.")
    private PriceDTO price;

    @NotNull(message = "Bedrooms count cannot be null.")
    @Min(1)
    private Integer bedrooms;

    @NotNull(message = "Bathroom count cannot be null.")
    @Min(1)
    private Integer bathrooms;

    @NotNull(message = "Is Attached Bathroom cannot be null.")
    private Boolean isAttachedBathroom;

    @NotNull(message = "Balcony count cannot be null.")
    @Min(0)
    private Integer balconies;

    @NotNull(message = "Is Attached Balcony cannot be null.")
    private Boolean isAttachedBalcony;

    @NotNull(message = "private Property Floor cannot be null.")
    private Integer propertyFloor;

    @NotNull(message = "Total Floor cannot be null.")
    private Integer totalFloor;

    @NotBlank(message = "Facing cannot be null or blank.")
    private String facing;

    @NotNull(message = "Is Main Road Facing cannot be null.")
    private Boolean isMainRoadFacing;

    @NotNull(message = "Build Up Area cannot be null.")
    private Integer buildUpArea;

    @NotNull(message = "Carpet Area cannot be null.")
    private Integer carpetArea;

    @NotNull(message = "Property Age cannot be null.")
    private Integer propertyAge;

    @NotNull(message = "Available From cannot be null.")
    private LocalDate availableFrom;

    @NotNull(message = "Notice Period cannot be null.")
    private Integer noticePeriodInMonths;

    @NotNull(message = "Gated Security Details cannot be null.")
    private Boolean gatedSecurity;

    @NotNull(message = "Gym Details cannot be null.")
    private Boolean gym;

    @NotNull(message = "Only Veg Details cannot be null.")
    Boolean onlyVeg;

    private Set<Integer> amenities;

    @NotNull(message = "Preferred Tenants Details cannot be null.")
    private Set<Property.PreferredTenants> preferredTenants;

    @NotNull(message = "Property Category cannot be null or blank.")
    private Property.PropertyCategory propertyCategory;

    @NotNull(message = "Water Supply Details cannot be null.")
    private Property.WaterSupply waterSupply;

    @NotNull(message = "Listing Category Details cannot be null.")
    private Property.ListingCategory listingCategory;

    @NotNull(message = "Property Type Details cannot be null.")
    private Property.PropertyType propertyType;

    @NotNull(message = "Furnished Status cannot be null.")
    private Property.FurnishedStatus furnishedStatus;

    @NotNull(message = "Possession Status cannot be null.")
    String possessionStatus;

    @NotNull(message = "Parking Details cannot be null.")
    private String parking;
}
