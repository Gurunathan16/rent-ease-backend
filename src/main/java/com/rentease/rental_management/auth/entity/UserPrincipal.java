package com.rentease.rental_management.auth.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails
{
    private final Users users;

    public UserPrincipal(Users users)
    {
        this.users = users;
    }

    public Integer getId()
    {
        return users.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return users.getLastLoginDate() != null && users.getLastLoginDate().plusDays(180).isAfter(LocalDate.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !users.getIsAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return users.getPasswordExpiryDate() != null && users.getPasswordExpiryDate().isAfter(LocalDate.now());
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
