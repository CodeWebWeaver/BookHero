package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.user.UserRegistrationRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationResponseDto;
import com.parkhomovsky.bookstore.exception.RegistrationException;

public interface UserService {
    public UserRegistrationResponseDto register(
            UserRegistrationRequestDto requestDto) throws RegistrationException;
}
