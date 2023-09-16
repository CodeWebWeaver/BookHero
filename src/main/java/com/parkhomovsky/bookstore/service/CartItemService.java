package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.exception.InvalidRequestParametersException;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;

public interface CartItemService {
    CartItemDto add(Long id, AddCartItemRequestDto cartItemDto)
            throws UserNotAuthenticatedException, InvalidRequestParametersException;

    CartItemDto create(CreateCartItemRequestDto cartItemDto)
            throws UserNotAuthenticatedException, InvalidRequestParametersException;

    CartItemDto delete(Long cartItemId)
            throws UserNotAuthenticatedException, InvalidRequestParametersException;
}
