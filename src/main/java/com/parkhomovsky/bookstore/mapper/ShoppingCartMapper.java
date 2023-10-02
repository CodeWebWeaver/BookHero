package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "cartItemDtos", ignore = true)
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    ShoppingCart toModel(ShoppingCartDto shoppingCartDto);
}
