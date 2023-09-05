package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.BookDto;
import com.parkhomovsky.bookstore.dto.BookSearchParameters;
import com.parkhomovsky.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto create(CreateBookRequestDto book);

    List<BookDto> getAll();

    BookDto getBookById(Long id);

    void deleteById(Long id);

    List<BookDto> bookSearch(BookSearchParameters parameters);
}
