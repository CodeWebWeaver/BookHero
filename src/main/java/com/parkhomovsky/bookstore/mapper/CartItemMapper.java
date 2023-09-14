package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CartItemMapper {
  CartItemDto toDto(CartItem cartItem);

  CartItem toModel(CreateCartItemRequestDto createCartItemRequestDto);

  CartItem toModel(AddCartItemRequestDto createCartItemRequestDto);
}
