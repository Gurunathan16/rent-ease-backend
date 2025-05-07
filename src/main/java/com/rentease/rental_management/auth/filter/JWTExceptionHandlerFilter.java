package com.rentease.rental_management.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JWTExceptionHandlerFilter extends OncePerRequestFilter
{
    private final ObjectMapper objectMapper;

    public JWTExceptionHandlerFilter(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        try
        {
            filterChain.doFilter(request, response);
        }
        catch(ExpiredJwtException exception)
        {
            buildResponse(response, HttpStatus.UNAUTHORIZED, "Token Expired. Try hitting /auth/refreshToken or login again.");
        }
        catch(MalformedJwtException exception)
        {
            buildResponse(response, HttpStatus.BAD_REQUEST, "Invalid token structure. " + exception.getMessage());
        }
        catch(UnsupportedJwtException exception)
        {
            buildResponse(response, HttpStatus.BAD_REQUEST, "Unsupported token format. " + exception.getMessage());
        }
        catch(SignatureException exception)
        {
            buildResponse(response, HttpStatus.UNAUTHORIZED, "Invalid token signature. " + exception.getMessage());
        }
        catch(BadCredentialsException exception)
        {
            buildResponse(response, HttpStatus.UNAUTHORIZED, exception.getMessage());
        }
        catch(JwtException exception)
        {
            buildResponse(response, HttpStatus.UNAUTHORIZED, "Token processing error." + exception.getMessage());
        }
    }

    private void buildResponse(HttpServletResponse response, HttpStatus httpStatus, String message) throws IOException{

        response.setStatus(httpStatus.value());
        response.setContentType("application/json");

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("Status", httpStatus.value());
        responseBody.put("error", httpStatus.getReasonPhrase());
        responseBody.put("message", message);
        responseBody.put("timestamp", Instant.now().toString());

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

}
