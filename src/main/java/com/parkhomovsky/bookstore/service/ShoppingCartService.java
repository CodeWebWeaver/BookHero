package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;

public interface ShoppingCartService {
  ShoppingCartDto getShoppingCart() throws UserNotAuthenticatedException;
}
