package com.rentease.rental_management.rent.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price
{
    private Double expectedPrice;

    private Boolean isPriceNegotiable;

    private Double securityDeposit;

    private Boolean callForPrice;

    private Double bookingAmount;

    private Double maintenanceFee;

    private Integer maintenanceFeeCycle;

}
