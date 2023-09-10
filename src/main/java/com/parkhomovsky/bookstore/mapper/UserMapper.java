package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.user.UserLoginResponseDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationResponseDto;
import com.parkhomovsky.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    UserRegistrationResponseDto toRegistrationResponse(User user);

    UserLoginResponseDto toLoginResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
