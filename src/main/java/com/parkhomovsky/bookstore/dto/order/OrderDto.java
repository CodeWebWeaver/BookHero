package com.parkhomovsky.bookstore.dto.order;

import com.parkhomovsky.bookstore.dto.order_item.OrderItemDto;
import com.parkhomovsky.bookstore.enums.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItemsDtos;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Status status;
}
