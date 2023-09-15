package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;

public interface CartItemService {
    CartItemDto add(Long id, AddCartItemRequestDto cartItemDto)
            throws UserNotAuthenticatedException;

    CartItemDto create(CreateCartItemRequestDto cartItemDto)
            throws UserNotAuthenticatedException;

    CartItemDto delete(Long cartItemId) throws UserNotAuthenticatedException;
}
