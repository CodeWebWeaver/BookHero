package com.parkhomovsky.bookstore.dto.order;

import com.parkhomovsky.bookstore.enums.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UpdateResponseDto {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Status status;
}
