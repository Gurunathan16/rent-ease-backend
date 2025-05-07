package com.rentease.rental_management.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    @Column(name = "username", unique = true)
    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private Boolean isEmailVerified;

    private Boolean isPhoneNumberVerified;

    private Boolean isAccountLocked;

    private LocalDate lastLoginDate;

    private LocalDate passwordExpiryDate;

}
