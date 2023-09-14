package com.parkhomovsky.bookstore.dto.item;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {
  @NotNull
  Long bookId;
  @NotNull
  int quantity;
}
