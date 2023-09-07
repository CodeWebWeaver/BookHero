package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.BookDto;
import com.parkhomovsky.bookstore.dto.BookSearchParameters;
import com.parkhomovsky.bookstore.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto create(CreateBookRequestDto book);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    List<BookDto> bookSearch(BookSearchParameters parameters);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> getAll(Pageable pageable);
}
