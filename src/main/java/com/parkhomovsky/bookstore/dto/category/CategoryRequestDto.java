package com.parkhomovsky.bookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryRequestDto {
    @NotNull(message = "Category cannot be null")
    @Length(min = 2, max = 255, message = "Author must be between 2 and 255 characters")
    private String name;
    @Length(max = 1000, message = "Description must be less than or equal to 1000 characters")
    private String description;
}
