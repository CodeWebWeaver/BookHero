package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.model.Book;
import java.util.List;

public interface BookService {
    Book create(Book book);

    List<Book> getAll();
}
