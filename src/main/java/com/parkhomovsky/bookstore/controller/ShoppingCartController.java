package com.parkhomovsky.bookstore.controller;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.service.CartItemService;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
@Tag(name = "Shopping cart management",
        description = "Endpoints for managing shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    @Operation(summary = "Retrieve user`s shopping cart",
            description = "Retrieve cart and items set in shopping cart")
    public ShoppingCartDto getUserShoppingCart() {
        return shoppingCartService.getUserShoppingCart();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new shopping cart item",
            description = "Create a new cart item and add it to the shopping cart")
    public CartItemDto createNewCartItem(
            @RequestBody @Valid CreateCartItemRequestDto createCartItemRequestDto) {
        return cartItemService.create(createCartItemRequestDto);
    }

    @PutMapping("cart-items/{cartItemId}")
    @Operation(summary = "Add item quantity",
            description = "Update quantity field for item in database")
    public CartItemDto updateCartItemQuantity(@PathVariable Long cartItemId,
                                      @Valid @RequestBody
                                      AddCartItemRequestDto addCartItemRequestDto) {
        return cartItemService.add(cartItemId, addCartItemRequestDto);
    }

    @DeleteMapping("cart-items/{cartItemId}")
    @Operation(summary = "Remove item from Shopping Cart",
            description = "Delete item from current user`s shopping cart")
    public CartItemDto deleteCartItem(@PathVariable Long cartItemId) {
        return cartItemService.delete(cartItemId);
    }
}
