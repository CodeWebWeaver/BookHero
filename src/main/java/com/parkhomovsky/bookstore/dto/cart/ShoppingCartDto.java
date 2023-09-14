package com.parkhomovsky.bookstore.dto.cart;

import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
  Long id;
  Long user_id;
  Set<CartItemDto> cartItemDtos;
}
