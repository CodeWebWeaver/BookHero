package com.parkhomovsky.bookstore.dto.exception;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
}
