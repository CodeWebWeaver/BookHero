package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;

public interface CartItemService {
    CartItemDto add(Long id, AddCartItemRequestDto cartItemDto);

    CartItemDto create(CreateCartItemRequestDto cartItemDto);

    CartItemDto delete(Long cartItemId);
}
