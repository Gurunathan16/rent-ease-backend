package com.rentease.rental_management.auth.controller;

import com.rentease.rental_management.auth.dto.*;
import com.rentease.rental_management.auth.service.impl.UsersServiceImpl;
import com.rentease.rental_management.util.response.ResponseEntityHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UsersController {
    private final UsersServiceImpl usersService;

    public UsersController(UsersServiceImpl usersService)
    {
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@Valid @RequestBody UsersLogin usersLogin, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.login(usersLogin);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> userSignup(@Valid @RequestBody UsersRegistration usersRegistration, BindingResult bindingResult)  {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.register(usersRegistration);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> userUpdate(@Valid @RequestBody UsersUpdate usersUpdate, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.update(usersUpdate);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Map<String, Object>> passwordUpdate(@Valid @RequestBody UsersPasswordChange usersPasswordChange, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.updatePassword(usersPasswordChange);
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile()
    {
        return usersService.viewProfile();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> userDelete()
    {
        return usersService.delete();
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Map<String, Object>> getRefreshToken(@Valid @RequestBody TokenRequest tokenRequest, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.refreshTokens(tokenRequest);
    }

    @PostMapping("/sendOTP")
    public ResponseEntity<Map<String, Object>> sendOTP(@Valid @RequestBody OTPRequest OTPRequestObject,
                                                       BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.sendOTP(OTPRequestObject.email());
    }

    @PostMapping("/verifyEmailOTP")
    public ResponseEntity<Map<String, Object>> verifyEmailOTP(@Valid @RequestBody VerifyEmailOTPRequest verifyEmailOTPRequest, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.verifyEmailOTP(verifyEmailOTPRequest);
    }

    private ResponseEntity<Map<String, Object>> validationErrorBuilder(BindingResult bindingResult)
    {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Validation check failed.",
                "Validation Errors", bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
    }

}
