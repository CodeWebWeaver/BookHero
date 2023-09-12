package com.parkhomovsky.bookstore.controller;

import com.parkhomovsky.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.parkhomovsky.bookstore.dto.category.CategoryDto;
import com.parkhomovsky.bookstore.dto.category.CategoryRequestDto;
import com.parkhomovsky.bookstore.service.BookService;
import com.parkhomovsky.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
@Tag(name = "Categories management",
        description = "Endpoints for managing categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new category",
            description = "Create a new category and add it to the database")
    private CategoryDto create(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return categoryService.create(categoryRequestDto);
    }

    @GetMapping
    @Operation(summary = "Retrieve all categories", description = "Get list of all categories")
    private List<CategoryDto> getAllCategories() {
        return categoryService.getAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category",
            description = "Update category in database with provided id and data")
    private CategoryDto update(@PathVariable Long id,
                               @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.update(id, categoryRequestDto);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete category",
            description = "Delete category from database")
    private void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category",
            description = "Gather all books by defined category id")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.getAllBooksByCategory(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id",
            description = "Retrieve category by id from database")
    public CategoryDto getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }
}
