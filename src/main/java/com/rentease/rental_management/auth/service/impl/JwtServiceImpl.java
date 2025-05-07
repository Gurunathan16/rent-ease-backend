package com.rentease.rental_management.auth.service.impl;

import com.rentease.rental_management.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService
{
    private Claims claims = null;
    private final String signKey;

    public JwtServiceImpl() throws NoSuchAlgorithmException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");

        SecretKey secretKey = keyGenerator.generateKey();

        signKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    @Override
    public String generateAccessToken(String username)
    {
        Map<String, Object> inputClaims = new HashMap<>();

        return Jwts
                .builder()
                .claims().add(inputClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 900000))
                .and()
                .signWith(getKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(String username)
    {
        Map<String, Object> inputClaims = new HashMap<>();

        return Jwts
                .builder()
                .claims().add(inputClaims)
                .subject(username)
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(Instant.now().plus(Duration.ofDays(7))))
                .and()
                .signWith(getKey())
                .compact();
    }

    @Override
    public Boolean verifyToken(String accessToken, UserDetails userDetails)
    {
        String username = extractUsername(accessToken);

        return username.equals(userDetails.getUsername());
    }

    @Override
    public String extractUsername(String accessToken)
    {
        claims = extractClaims(accessToken);

        return claimResolver(Claims::getSubject);
    }


    @Override
    public Claims getClaims(String accessToken)
    {
        return extractClaims(accessToken);
    }

    @Override
    public Claims extractClaims(String accessToken)
    {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }

    @Override
    public <T> T claimResolver(Function<Claims, T> extractClaim)
    {
        return extractClaim.apply(claims);
    }

    private SecretKey getKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(signKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
