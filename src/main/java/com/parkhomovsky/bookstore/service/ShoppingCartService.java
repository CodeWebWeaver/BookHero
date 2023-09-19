package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import java.util.Set;

public interface ShoppingCartService {
    ShoppingCartDto getUserShoppingCartDto() throws UserNotAuthenticatedException;

    ShoppingCart getUserShoppingCart()
            throws UserNotAuthenticatedException, EntityNotFoundException;

    Set<CartItem> getCartItemsSetForShoppingCart(ShoppingCart shoppingCart);

    void clearShoppingCart() throws UserNotAuthenticatedException;
}
