package com.parkhomovsky.bookstore.controller;

import com.parkhomovsky.bookstore.dto.book.BookDto;
import com.parkhomovsky.bookstore.dto.book.BookSearchParameters;
import com.parkhomovsky.bookstore.dto.book.CreateBookRequestDto;
import com.parkhomovsky.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(value = "/books")
@Tag(name = "Book management",
        description = "Endpoints for managing products")
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new book",
            description = "Create a new book and add it to the database")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto book) {
        return bookService.create(book);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id",
            description = "Retrieve a book by its unique id.")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books",
            description = "Search for books by title or author.")
    public List<BookDto> search(BookSearchParameters parameters) {
        return bookService.search(parameters);
    }

    @GetMapping
    @Operation(summary = "List Books with pagination",
            description = "List books with pagination using.")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update Book Details",
            description = "Update the details of an existing book.")
    public BookDto update(
            @PathVariable Long id, @RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.update(id, createBookRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mark Book as Deleted",
            description = "Mark a book as 'deleted' in the database.")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
