package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.category.CategoryDto;
import com.parkhomovsky.bookstore.dto.category.CategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto create(CategoryRequestDto categoryRequestDto);

    void delete(Long id);

    CategoryDto update(Long id, CategoryRequestDto categoryRequestDto);

    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto getById(Long id);
}
