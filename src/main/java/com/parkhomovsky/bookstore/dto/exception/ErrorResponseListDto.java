package com.parkhomovsky.bookstore.dto.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
public class ErrorResponseListDto {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private List<String> error;
}
