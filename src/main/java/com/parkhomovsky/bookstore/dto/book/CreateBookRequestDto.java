package com.parkhomovsky.bookstore.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title cannot be empty")
    @Length(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    private String title;
    @NotBlank(message = "Author cannot be empty")
    @Length(min = 2, max = 255, message = "Author must be between 2 and 255 characters")
    private String author;
    @NotBlank(message = "ISBN cannot be empty")
    @ISBN(message = "Invalid ISBN format")
    private String isbn;
    @NotBlank(message = "Price cannot be empty")
    @DecimalMin(value = "0.00", message = "Price must be greater than or equal to 0.00")
    private BigDecimal price;
    @Length(max = 1000, message = "Description must be less than or equal to 1000 characters")
    private String description;
    @URL(message = "Invalid URL format")
    private String coverImage;
    @NotBlank(message = "At least one category id needed")
    private Set<Long> categoryId;
}
