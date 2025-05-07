package com.rentease.rental_management.auth.config;

import com.rentease.rental_management.auth.filter.JWTExceptionHandlerFilter;
import com.rentease.rental_management.auth.filter.JWTFilter;
import com.rentease.rental_management.auth.service.impl.UserDetailsServiceHelper;
import com.rentease.rental_management.util.exception.CustomAuthEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig
{
    private final JWTExceptionHandlerFilter jwtExceptionHandlerFilter;
    private final JWTFilter jwtFilter;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsServiceHelper userDetailsServiceHelper;

    public SecurityConfig(JWTExceptionHandlerFilter jwtExceptionHandlerFilter, JWTFilter jwtFilter, CustomAuthEntryPoint customAuthEntryPoint, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsServiceHelper userDetailsServiceHelper)
    {

        this.jwtExceptionHandlerFilter = jwtExceptionHandlerFilter;
        this.jwtFilter = jwtFilter;
        this.customAuthEntryPoint = customAuthEntryPoint;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsServiceHelper = userDetailsServiceHelper;
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
                .cors(corsConfiguration -> corsConfiguration
                        .configurationSource(getCorsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/auth/login", "/auth/signup", "/auth/changePassword", "/auth/sendOTP",
                                "/auth/verifyEmailOTP", "/auth/refreshToken",
                                "/app/properties", "/app/localitySearch/{locality}", "/app/filter", "/app/view",
                                "/app/likeCount").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtExceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .authenticationEntryPoint(customAuthEntryPoint))
                .build();
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceHelper);

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource getCorsConfigurationSource()
    {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

}
