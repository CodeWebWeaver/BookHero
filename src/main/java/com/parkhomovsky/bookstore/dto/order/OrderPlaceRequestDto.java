package com.parkhomovsky.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class OrderPlaceRequestDto {
    @NotBlank
    @Length(min = 5, max = 255)
    private String shippingAddress;
}
