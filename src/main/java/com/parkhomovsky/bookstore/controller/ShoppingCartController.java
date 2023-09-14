package com.parkhomovsky.bookstore.controller;

import com.parkhomovsky.bookstore.service.CartItemService;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
@Tag(name = "Shopping сart management",
        description = "Endpoints for managing shopping сart")
public class ShoppingCartController {
  private final ShoppingCartService shoppingCartService;
  private final CartItemService cartItemService;
/*
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create new cart item",
          description = "Create a new cart item and add it to the shopping cart")
  public CategoryDto createNewCartItem(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
    return cartItemService.createCartItem(categoryRequestDto);
  }

  @GetMapping
  @Operation(summary = "Retrieve all categories", description = "Get list of all categories")
  public List<CategoryDto> getAllCategories(Pageable pageable) {
    return categoryService.getAll(pageable);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Update category",
          description = "Update category in database with provided id and data")
  public CategoryDto update(@PathVariable Long id,
                            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
    return categoryService.update(id, categoryRequestDto);
  }

  @PostMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "Delete category",
          description = "Delete category from database")
  public void delete(@PathVariable Long id) {
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
  */
}
