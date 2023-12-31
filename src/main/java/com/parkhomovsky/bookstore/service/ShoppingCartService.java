package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartDto getUserShoppingCartDto();

    void clearShoppingCart();

    ShoppingCart getShoppingCart();

}
