package com.rentease.rental_management.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAsParty
{
    private String firstName;

    private String lastName;

    private String gender;

    private String email;

    private String phoneNumber;

    private Boolean isEmailVerified;

    private Boolean isPhoneNumberVerified;
}
