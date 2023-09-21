package com.parkhomovsky.bookstore.dto.item;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AddCartItemRequestDto {
    @Min(value = 1, message = "Quantity should be at least 1 for creating")
    private int quantity;
}
