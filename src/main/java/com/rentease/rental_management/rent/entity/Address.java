package com.rentease.rental_management.rent.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address
{
    private String fullAddress;

    private String subLocality;

    private String locality;

    private String city;

    private String state;

    private Integer pinCode;

    private Double latitude;

    private Double longitude;

}
