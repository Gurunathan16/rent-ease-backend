package com.rentease.rental_management.auth.service.impl;

import com.rentease.rental_management.auth.dto.*;
import com.rentease.rental_management.auth.entity.Users;
import com.rentease.rental_management.auth.repository.UsersRepository;
import com.rentease.rental_management.auth.service.RedisService;
import com.rentease.rental_management.auth.service.UsersService;
import com.rentease.rental_management.util.exception.TooManyRequestsException;
import com.rentease.rental_management.util.mappers.UsersMapper;
import com.rentease.rental_management.util.response.CustomResponseCookieHandler;
import com.rentease.rental_management.util.response.ResponseEntityHandler;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersServiceImpl implements UsersService
{
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final Long maximumSessions;
    private final RedisService redisService;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;

    public UsersServiceImpl(JwtServiceImpl jwtService, AuthenticationManager authenticationManager,
                            @Value("${session.maximumSessions}")Long maximumSessions, RedisService redisService,
                            UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                            JavaMailSender javaMailSender)
    {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.maximumSessions = maximumSessions;
        this.redisService = redisService;
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public ResponseEntity<Map<String, Object>> login(UsersLogin usersLogin)
    {
        final String username = usersLogin.username();

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                usersLogin.password()));

        if(authentication.isAuthenticated())
        {
            redisService.evictIfSessionLimitReached(username, maximumSessions);

            List<String> generatedToken = generateTokens(username);

            redisService.saveRefreshToken(username, generatedToken.get(1));

            Users users = usersRepository.findByUsername(username);

            if(!users.getLastLoginDate().equals(LocalDate.now()))
            {
                users.setLastLoginDate(LocalDate.now());
                users.setPasswordExpiryDate(LocalDate.now().plusDays(90));

                usersRepository.save(users);
            }

            return responseCookieGenerator(generatedToken);
        }
        else
        {
            return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Authentication Failed.", "Recovery", "Check your credentials");
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> register(UsersRegistration usersRegistration) {
        if(usersRepository.existsByUsername(usersRegistration.getUsername()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.CONFLICT, "Username already in use.", "Recovery", "Retry with different username.");

        if(usersRepository.existsByEmail(usersRegistration.getEmail()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.CONFLICT, "Email ID already in use.", "Recovery", "Try login with existing account.");

        if(usersRepository.existsByPhoneNumber(usersRegistration.getPhoneNumber()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.CONFLICT, "Phone Number already in use.",
                    "Recovery", "Try login with existing account.");

        if(!usersRegistration.getPassword().equals(usersRegistration.getConfirmPassword()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Password - Confirm Password Mismatch", "Recovery", "Password and Confirm Password should be same.");

        usersRegistration.setPassword(bCryptPasswordEncoder.encode(usersRegistration.getPassword()));

        usersRepository.save(UsersMapper.UsersRegistrationToUsers(usersRegistration));

        sendOTP(usersRegistration.getEmail());

        return ResponseEntityHandler.getResponseEntity(HttpStatus.CREATED, "User registered successfully. Please verify your email.", "Details", usersRegistration.getFirstName());
    }

    @Override
    public ResponseEntity<Map<String, Object>> sendOTP(String email) {

        if(email != null && redisService.isEmailCooldownOn(email))
            throw new TooManyRequestsException("Please wait for 1 minute before requesting for new Email " +
                    "verification OTP");

        if(usersRepository.findByEmail(email).getIsEmailVerified())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Email Id already verified.", "Action", "Continue looking for properties.");

        /*if(phoneNumber != null && redisService.isPhoneNumberCooldownOn(phoneNumber))
            throw new TooManyRequestsException("Please wait for 1 minute before requesting for new Phone number " +
                    "verification OTP");*/

        String otp = generateOTP();

        sendEmailOTP(email, otp);

        /*if(phoneNumber == null)
            sendEmailOTP(email, otp);
        else
            sendPhoneOTP(phoneNumber, otp);*/

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "OTP sent to email successfully.", "Action", "Verify the OTP");

    }

    @Override
    public ResponseEntity<Map<String, Object>> verifyEmailOTP(VerifyEmailOTPRequest verifyEmailOTPRequest)
    {
        String savedOTP = redisService.getEmailOTP(verifyEmailOTPRequest.email());

        if(savedOTP == null || !savedOTP.equals(verifyEmailOTPRequest.otp()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Invalid or Expired OTP", "Recovery", "Try hitting /auth/sendOTP");

        Users users = usersRepository.findByEmail(verifyEmailOTPRequest.email());

        if(users == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "User account not exists.", "Recovery", "Try hitting /auth/signup to register yourself.");

        users.setIsEmailVerified(true);

        if(users.getIsPhoneNumberVerified())
            users.setIsAccountLocked(false);

        usersRepository.save(users);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Email verified successfully.",
                "Details", users.getFirstName());
    }

    /*
    @Override
    public ResponseEntity<Map<String, Object>> verifyPhoneOTP(VerifyPhoneOTPRequest verifyPhoneOTPRequest)
    {
        String savedOTP = redisService.getPhoneNumberOTP(verifyPhoneOTPRequest.phoneNumber());

        if(savedOTP == null || !savedOTP.equals(verifyPhoneOTPRequest.otp()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Invalid or Expired OTP",
                    "Recovery", "Try hitting /auth/sendOTP");

        Users users = usersRepository.findByPhoneNumber(verifyPhoneOTPRequest.phoneNumber());

        if(users == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "User account not exists.", "Recovery", "Try hitting /auth/signup to register yourself.");

        users.setIsPhoneNumberVerified(true);

        if(users.getIsEmailVerified())
            users.setIsAccountLocked(false);

        usersRepository.save(users);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Phone Number verified successfully.",
                "Details", users.getFirstName());
    }*/

    @Override
    public ResponseEntity<Map<String, Object>> update(UsersUpdate usersUpdate)
    {
        Users users = getUsers();

        usersRepository.save(UsersMapper.UsersUpdateToUsers(users, usersUpdate));

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "User Profile updated successfully", "Details", users.getFirstName());

    }

    @Override
    public ResponseEntity<Map<String, Object>> updatePassword(UsersPasswordChange usersPasswordChange)
    {
        if(!usersPasswordChange.password().equals(usersPasswordChange.confirmPassword()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Password - Confirm Password Mismatch", "Recovery", "Password and Confirm Password should be same.");

        Users users = getUsers();

        if(bCryptPasswordEncoder.matches(usersPasswordChange.oldPassword(), users.getPassword()))
        {
            users.setPassword(bCryptPasswordEncoder.encode(usersPasswordChange.password()));

            usersRepository.save(users);

            updateLoginDate();

            return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Change Password request processed successfully.", "Details", users.getFirstName());
        }
        else
            return ResponseEntityHandler.getResponseEntity(HttpStatus.FORBIDDEN, "Old Password - Incorrect", "Recovery", "Enter correct Old Password");
    }

    @Override
    public ResponseEntity<Map<String, Object>> viewProfile()
    {
        Users users = getUsers();

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK,
                "User profile fetch successful.",
                "Details", UsersMapper.UsersToUsersProfile(users));
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> delete()
    {
        final String username = getUsername();

        if(usersRepository.existsByUsername(username))
            usersRepository.deleteByUsername(username);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK,
                "Profile Deleted SuccessFully.",
                "Home", "/app/properties");
    }

    @Override
    public ResponseEntity<Map<String, Object>> refreshTokens(TokenRequest tokenRequest)
    {
        String oldRefreshToken = tokenRequest.refreshToken();
        String username = jwtService.extractUsername(oldRefreshToken);

        if(!redisService.isRefreshTokenValid(username, oldRefreshToken))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.UNAUTHORIZED, "Refresh token expired.", "Recovery", "Please login again.");

        redisService.removeRefreshToken(username, oldRefreshToken);

        List<String> generatedTokens = generateTokens(username);

        redisService.saveRefreshToken(username, generatedTokens.get(1));

        return responseCookieGenerator(generatedTokens);
    }

    @Override
    public ResponseEntity<Map<String, Object>> responseCookieGenerator(List<String> generatedTokens)
    {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("code", HttpStatus.OK.value());
        response.put("status", "Success");
        response.put("message", "Tokens generated");
        response.put("payload", jwtService.getClaims(generatedTokens.get(0)));

        ResponseCookie accessCookie = CustomResponseCookieHandler.responseCookieBuilder("accessToken",
                generatedTokens.getFirst(), true, true, "Strict", "/", 900);

        ResponseCookie refreshCookie = CustomResponseCookieHandler.responseCookieBuilder("refreshToken",
                generatedTokens.get(1), true, true, "Strict", "/", 604800);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString()).body(response);
    }

    private String generateOTP()
    {
        SecureRandom secureRandom = new SecureRandom();

        return String.format("%06d", secureRandom.nextInt(1000000));
    }

    /*private void sendPhoneOTP(String phoneNumber, String otp)
    {
        final String FAST2SMS_URL = "https://www.fast2sms.com/dev/bulkV2";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", fast2SmsAPIKey);

        Map<String, Object> body = new HashMap<>();
        body.put("route", "q");
        body.put("variables_values", otp);
        body.put("numbers", phoneNumber);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(FAST2SMS_URL, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to send SMS: " + response.getBody());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error sending SMS: " + ex.getMessage(), ex);
        }
    }*/

    private void sendEmailOTP(String toEmail, String otp)
    {
        try
        {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("OTP for Login - " + otp);

            String content = "<p><strong>" + otp + "</strong> is your One time password(OTP). OTP expires within 5 minutes. Please do not " +
                    "share the OTP with others. </p><br><p>Regards, Team RentEase</b></p>";

            helper.setText(content, true);
            javaMailSender.send(message);

            redisService.saveEmailOTP(toEmail, otp);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to send email otp ", e);
        }
    }

    private List<String> generateTokens(String username)
    {
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        return List.of(accessToken, refreshToken);
    }

    private void updateLoginDate()
    {
        Users users = getUsers();

        if(!users.getLastLoginDate().equals(LocalDate.now()))
        {
            users.setLastLoginDate(LocalDate.now());
            users.setPasswordExpiryDate(LocalDate.now().plusDays(90));

            usersRepository.save(users);
        }
    }

    private Users getUsers()
    {
        final String username = getUsername();

        return usersRepository.findByUsername(username);
    }

    private String getUsername()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }
}
