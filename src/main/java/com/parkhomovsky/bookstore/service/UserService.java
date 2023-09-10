package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.user.UserRegistrationRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationResponseDto;

public interface UserService {
    public UserRegistrationResponseDto register(
            UserRegistrationRequestDto requestDto);
}
