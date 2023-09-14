package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface ShoppingCartMapper {
  ShoppingCartDto toDto(ShoppingCart shoppingCart);

  ShoppingCart toModel(ShoppingCartDto shoppingCartDto);
}
