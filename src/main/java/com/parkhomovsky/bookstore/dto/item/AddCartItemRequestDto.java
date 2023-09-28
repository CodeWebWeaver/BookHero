package com.parkhomovsky.bookstore.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCartItemRequestDto {
    @NotNull
    @Min(value = 1, message = "Quantity should be at least 1 for creating")
    private int quantity;
}
