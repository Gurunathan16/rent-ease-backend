package com.rentease.rental_management.auth.filter;

import com.rentease.rental_management.auth.service.impl.JwtServiceImpl;
import com.rentease.rental_management.auth.service.impl.UserDetailsServiceHelper;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JWTFilter extends OncePerRequestFilter
{
    private final JwtServiceImpl jwtService;
    private final UserDetailsServiceHelper userDetailsServiceHelper;

    public JWTFilter(JwtServiceImpl jwtService, UserDetailsServiceHelper userDetailsServiceHelper)
    {
        this.jwtService = jwtService;
        this.userDetailsServiceHelper = userDetailsServiceHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String accessToken = null;
        String username = null;

        if(header != null && header.startsWith("Bearer "))
        {
            Pattern pattern = Pattern.compile("^Bearer\\s+(\\S+)$");
            Matcher matcher = pattern.matcher(header);

            if(matcher.find())
            {
                accessToken = matcher.group(1);
                username = jwtService.extractUsername(accessToken);
            }
            else
                throw new MalformedJwtException("Check the format of JWT Token sent.");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = userDetailsServiceHelper.loadUserByUsername(username);

            if(jwtService.verifyToken(accessToken, userDetails))
            {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else
            {
                throw new BadCredentialsException("Invalid token or user mismatch.");
            }
        }

        filterChain.doFilter(request, response);
    }

}
