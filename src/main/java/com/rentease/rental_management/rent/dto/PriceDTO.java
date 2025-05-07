package com.rentease.rental_management.rent.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO
{
    @NotNull(message = "Expected Price cannot be null.")
    @Min(0)
    private Double expectedPrice;

    @NotNull(message = "Price Negotiable Details cannot be null.")
    private Boolean isPriceNegotiable;

    @NotNull(message = "Security Deposit cannot be null.")
    private Double securityDeposit;

    @NotNull(message = "Call For Price Details cannot be null.")
    private Boolean callForPrice;

    private Double bookingAmount;

    private Double maintenanceFee;

    private Integer maintenanceFeeCycle;

}
