package com.rentease.rental_management.util.response;

import org.springframework.http.ResponseCookie;

public class CustomResponseCookieHandler
{
    public static ResponseCookie responseCookieBuilder(String cookieName, String cookie, Boolean httpOnly,
                                                      Boolean secure,
                                              String sameSite, String path, long maxAge)
    {
        return ResponseCookie
                .from(cookieName, cookie)
                .httpOnly(httpOnly)
                .secure(secure)
                .sameSite(sameSite)
                .path(path)
                .maxAge(maxAge)
                .build();
    }

}
