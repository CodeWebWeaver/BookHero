package com.parkhomovsky.bookstore.service;


import com.parkhomovsky.bookstore.dto.user.UserLoginRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto requestDto);
}