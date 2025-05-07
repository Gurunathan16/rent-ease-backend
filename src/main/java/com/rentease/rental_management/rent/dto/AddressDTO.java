package com.rentease.rental_management.rent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO
{
    @NotBlank(message = "Address cannot be null or blank.")
    @Size(min = 30, max = 100, message = "Address cannot be less than 30 characters and more than 100 characters.")
    private String fullAddress;

    @NotBlank(message = "Sub-Locality cannot be null or blank.")
    @Size(min = 5, max = 20, message = "Sub-Locality cannot be less than 5 characters and more than 20 " +
            "characters.")
    private String subLocality;

    @NotBlank(message = "Locality cannot be null or blank.")
    @Size(min = 5, max = 20, message = "Locality cannot be less than 5 characters and more than 20 characters.")
    private String locality;

    @NotBlank(message = "City cannot be null or blank.")
    @Size(min = 5, max = 20, message = "City cannot be less than 5 characters and more than 20 characters.")
    public String city;

    @NotBlank(message = "State cannot be null or blank.")
    @Size(min = 3, max = 25, message = "State cannot be less than 3 characters and more than 25 characters.")
    private String state;

    @NotNull(message = "Pin code cannot be null.")
    @Pattern(regexp = "^\\d{6}$")
    private Integer pinCode;

    @NotNull(message = "Latitude cannot be null.")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null.")
    private Double longitude;
}
