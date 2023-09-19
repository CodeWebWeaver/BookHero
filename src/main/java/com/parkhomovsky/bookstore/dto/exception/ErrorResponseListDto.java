package com.parkhomovsky.bookstore.dto.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseListDto {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private List<String> error;
}
