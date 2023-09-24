package com.parkhomovsky.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@Accessors(chain = true)
public class CategoryRequestDto {
    @NotBlank(message = "Category cannot be empty")
    @Length(min = 2, max = 255, message = "Author must be between 2 and 255 characters")
    private String name;
    @Length(max = 1000, message = "Description must be less than or equal to 1000 characters")
    private String description;
}
