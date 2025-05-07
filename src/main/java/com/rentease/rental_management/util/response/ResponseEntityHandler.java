package com.rentease.rental_management.util.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseEntityHandler
{
    public static ResponseEntity<Map<String, Object>> getResponseEntity(HttpStatus status, String message, String detailsName, Object details)
    {
        return ResponseEntity.status(status).body(ResponseEntityHandler.getResponseMap(status.value(),
                status.getReasonPhrase(),
                message,
                detailsName, details));
    }

    private static Map<String, Object> getResponseMap(Integer code, String status, String message, String detailsName,
                                                      Object details)
    {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("code", code);
        response.put("status", status);
        response.put("message", message);
        response.put(detailsName, details);

        return response;
    }
}
