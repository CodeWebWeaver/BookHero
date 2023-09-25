package com.parkhomovsky.bookstore.dto.order;

import com.parkhomovsky.bookstore.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StatusUpdateResponseDto {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private OrderStatus orderStatus;
}
