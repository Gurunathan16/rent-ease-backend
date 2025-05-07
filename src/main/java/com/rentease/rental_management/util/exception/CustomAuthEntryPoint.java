package com.rentease.rental_management.util.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint
{
    private final Map<String, Object> responseMap;
    private final ObjectMapper objectMapper;

    public CustomAuthEntryPoint()
    {
        responseMap = new LinkedHashMap<>();
        objectMapper = new ObjectMapper();
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        responseMap.put("status", HttpStatus.UNAUTHORIZED);

        if(request.getRequestURI().equals("/app/sellerDetails"))
            responseMap.put("message", "Please login to view seller details.");
        else
            responseMap.put("message", "Wrong token or not logged in.");

        responseMap.put("Recovery", "Try login by hitting /auth/login");

        response.getWriter().write(objectMapper.writeValueAsString(responseMap));

    }
}
