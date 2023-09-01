package com.parkhomovsky.bookstore.repository;

import com.parkhomovsky.bookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
