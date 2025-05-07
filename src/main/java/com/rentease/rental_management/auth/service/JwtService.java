package com.rentease.rental_management.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService
{
    String extractUsername(String accessToken);

    <T> T claimResolver(Function<Claims, T> extractClaim);

    Claims extractClaims(String accessToken);

    Claims getClaims(String accessToken);

    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    Boolean verifyToken(String accessToken, UserDetails userDetails);
}
