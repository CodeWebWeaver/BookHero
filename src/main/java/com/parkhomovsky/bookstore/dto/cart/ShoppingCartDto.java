package com.parkhomovsky.bookstore.dto.cart;

import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItemDtos;
}
