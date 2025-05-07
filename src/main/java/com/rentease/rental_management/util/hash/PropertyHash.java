package com.rentease.rental_management.util.hash;

import com.rentease.rental_management.rent.dto.PropertyHashDTO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PropertyHash
{
    public static String uniquePropertyHash(PropertyHashDTO propertyHash)
    {
        String info = propertyHash.fullAddress().trim().toLowerCase() + "|" +
                propertyHash.locality().trim().toLowerCase() + "|" +
                propertyHash.subLocality().trim().toLowerCase() + "|" +
                propertyHash.city().trim().toLowerCase() + "|" +
                propertyHash.pinCode() + "|" +
                propertyHash.listingCategory().trim().toLowerCase() + "|" +
                propertyHash.propertyType().trim().toLowerCase() + "|" +
                propertyHash.propertyCategory().trim().toLowerCase() + "|" +
                propertyHash.possessionStatus().trim().toLowerCase();

        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = messageDigest.digest(info.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder().encodeToString(hashBytes);

        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }

    }
}
