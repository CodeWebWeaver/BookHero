package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;

public interface CartItemService {
  CartItemDto addCartItem(Long id, AddCartItemRequestDto cartItemDto) throws UserNotAuthenticatedException;
  CartItemDto createCartItem(CreateCartItemRequestDto cartItemDto) throws UserNotAuthenticatedException;
}
