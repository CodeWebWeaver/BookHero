package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.BookDto;
import com.parkhomovsky.bookstore.dto.CreateBookRequestDto;
import com.parkhomovsky.bookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto book);
}
