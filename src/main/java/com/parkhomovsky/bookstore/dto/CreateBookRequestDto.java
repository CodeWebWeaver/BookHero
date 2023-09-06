package com.parkhomovsky.bookstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull(message = "Title cannot be null")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    private String title;
    @NotNull(message = "Author cannot be null")
    @Size(min = 2, max = 255, message = "Author must be between 2 and 255 characters")
    private String author;
    @NotNull(message = "ISBN cannot be null")
    @Pattern(regexp = "\\d{13}", message = "ISBN must be 13 digits")
    private String isbn;
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
    private BigDecimal price;
    @Size(max = 1000, message = "Description must be less than or equal to 1000 characters")
    private String description;
    @Size(max = 255, message = "Cover image URL must be less than or equal to 255 characters")
    private String coverImage;
}
