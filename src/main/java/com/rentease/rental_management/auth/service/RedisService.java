package com.rentease.rental_management.auth.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class RedisService
{
    RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    private String sessionKey(String username)
    {
        return "users_session" + username;
    }

    private String emailKey(String email)
    {
        return "email_otp" + email;
    }

    private String phoneNumberKey(String phoneNumber)
    {
        return "phoneNumber_otp" + phoneNumber;
    }

    private String emailCoolDownKey(String email)
    {
        return "email_Cooldown_otp" + email;
    }

    private String phoneNumberCoolDownKey(String phoneNumber)
    {
        return "phoneNumber_Cooldown_otp" + phoneNumber;
    }

    public void evictIfSessionLimitReached(String username, Long maximumSessions)
    {
        Long count = redisTemplate.opsForList().size(sessionKey(username));

        if(count != null && count >= maximumSessions)
            redisTemplate.opsForList().leftPop(sessionKey(username));
    }

    public void saveRefreshToken(String username, String refreshToken)
    {
        redisTemplate.opsForList().rightPush(sessionKey(username), refreshToken);
        redisTemplate.expire(sessionKey(username), Duration.ofDays(7));
    }

    public Boolean isRefreshTokenValid(String username, String oldRefreshToken)
    {
        List<Object> refreshTokens = redisTemplate.opsForList().range(sessionKey(username), 0, -1);

        return refreshTokens != null && refreshTokens.contains(oldRefreshToken);
    }

    public void removeRefreshToken(String username, String oldRefreshToken)
    {
        redisTemplate.opsForList().remove(sessionKey(username), 1L, oldRefreshToken);
    }

    public void removeUser(String username)
    {

        redisTemplate.delete(sessionKey(username));
    }

    public void saveEmailOTP(String email, String otp)
    {
        redisTemplate.opsForValue().set(emailKey(email), otp, Duration.ofMinutes(5));

        redisTemplate.opsForValue().set(emailCoolDownKey(email), "1", Duration.ofMinutes(1));
    }

    public boolean isEmailCooldownOn(String email)
    {
        return redisTemplate.hasKey(emailCoolDownKey(email));
    }

    public String getEmailOTP(String email)
    {
        return (String) redisTemplate.opsForValue().get(emailKey(email));
    }

    public void savePhoneNumberOTP(String phoneNumber, String otp)
    {
        redisTemplate.opsForValue().set(phoneNumberKey(phoneNumber), otp, Duration.ofMinutes(5));

        redisTemplate.opsForValue().set(phoneNumberCoolDownKey(phoneNumber), "1", Duration.ofMinutes(1));
    }

    public boolean isPhoneNumberCooldownOn(String phoneNumber)
    {
        return redisTemplate.hasKey(phoneNumberCoolDownKey(phoneNumber));
    }

    public String getPhoneNumberOTP(String phoneNumber)
    {
        return (String) redisTemplate.opsForValue().get(phoneNumberKey(phoneNumber));
    }

}
