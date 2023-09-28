package com.parkhomovsky.bookstore.dto.order;

import com.parkhomovsky.bookstore.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderUpdateStatusRequest {
    @NotNull
    private OrderStatus orderStatus;
}
