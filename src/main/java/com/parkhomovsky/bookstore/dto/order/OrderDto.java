package com.parkhomovsky.bookstore.dto.order;

import com.parkhomovsky.bookstore.dto.orderitem.OrderItemDto;
import com.parkhomovsky.bookstore.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private List<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private OrderStatus orderStatus;
}
