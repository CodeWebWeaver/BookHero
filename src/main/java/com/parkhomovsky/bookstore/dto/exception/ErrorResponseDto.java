package com.parkhomovsky.bookstore.dto.exception;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
}
