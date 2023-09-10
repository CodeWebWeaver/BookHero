package com.parkhomovsky.bookstore.controller;

import com.parkhomovsky.bookstore.dto.user.UserLoginRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserLoginResponseDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationResponseDto;
import com.parkhomovsky.bookstore.exception.RegistrationException;
import com.parkhomovsky.bookstore.service.AuthenticationService;
import com.parkhomovsky.bookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto requestDto
    ) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    @ResponseBody
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto requestDto
    )
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
