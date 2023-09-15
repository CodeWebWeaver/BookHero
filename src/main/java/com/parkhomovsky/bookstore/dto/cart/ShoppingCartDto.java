package com.parkhomovsky.bookstore.dto.cart;

import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItemDtos;
}
