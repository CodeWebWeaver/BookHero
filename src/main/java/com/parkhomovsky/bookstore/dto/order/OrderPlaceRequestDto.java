package com.parkhomovsky.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderPlaceRequestDto {
    @NotNull
    private String shippingAddress;
}
