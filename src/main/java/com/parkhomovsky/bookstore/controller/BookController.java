package com.parkhomovsky.bookstore.controller;

import com.parkhomovsky.bookstore.dto.BookDto;
import com.parkhomovsky.bookstore.dto.BookSearchParameters;
import com.parkhomovsky.bookstore.dto.CreateBookRequestDto;
import com.parkhomovsky.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
@Tag(name = "Book management", description = "Endpoints for managing products")
public class BookController {
    private final BookService bookService;

    @PutMapping("/{id}")
    public BookDto update(
            @PathVariable Long id, @RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.update(id, createBookRequestDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new book", description = "Adding new book to database")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto book) {
        return bookService.create(book);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Try to find book with provided id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books by title or author")
    public List<BookDto> search(BookSearchParameters parameters) {
        return bookService.bookSearch(parameters);
    }

    @GetMapping
    @Operation(summary = "List book using pagination", description =
            "List books on page using parameters")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.getAll(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete book by id", description = "Delete book from database")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
