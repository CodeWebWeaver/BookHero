package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.user.UserRegistrationRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationResponseDto;
import com.parkhomovsky.bookstore.exception.RegistrationException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserRegistrationResponseDto register(
            UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserDetails getUser();

    Long getUserId();
}
