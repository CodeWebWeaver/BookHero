package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationRequestDto;
import com.parkhomovsky.bookstore.dto.user.UserRegistrationResponseDto;
import com.parkhomovsky.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    UserRegistrationResponseDto toRegistrationResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toModel(UserRegistrationRequestDto requestDto);
}
