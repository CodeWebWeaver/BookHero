package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.book.BookDto;
import com.parkhomovsky.bookstore.dto.book.BookSearchParameters;
import com.parkhomovsky.bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto create(CreateBookRequestDto book);

    BookDto getById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters parameters);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> getAll(Pageable pageable);
}
