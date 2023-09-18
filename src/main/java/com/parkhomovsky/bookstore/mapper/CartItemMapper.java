package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemRequestDto createCartItemRequestDto);

    CartItem toModel(AddCartItemRequestDto createCartItemRequestDto);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CartItemDto cartItemDto);
}
