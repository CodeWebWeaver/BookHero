package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.BookDto;
import com.parkhomovsky.bookstore.dto.CreateBookRequestDto;
import com.parkhomovsky.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    Book toModel(CreateBookRequestDto book);
}
