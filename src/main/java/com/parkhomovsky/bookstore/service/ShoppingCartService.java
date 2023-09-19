package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import java.util.Set;
import com.parkhomovsky.bookstore.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartDto getUserShoppingCartDto() throws UserNotAuthenticatedException;

    Set<CartItem> getCartItemsSetForShoppingCart(ShoppingCart shoppingCart);

    void clearShoppingCart() throws UserNotAuthenticatedException;
    ShoppingCartDto getUserShoppingCart();

    ShoppingCart getShoppingCart();
}
