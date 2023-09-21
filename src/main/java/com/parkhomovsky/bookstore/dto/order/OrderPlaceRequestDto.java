package com.parkhomovsky.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class OrderPlaceRequestDto {
    @NotBlank
    @Length(min = 5, max = 255)
    private String shippingAddress;
}
