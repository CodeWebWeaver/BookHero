package com.parkhomovsky.bookstore.dto.item;

import lombok.Data;

@Data
public class CartItemDto {
  Long id;
  Long bookId;
  String bookTitle;
  int quantity;
}
