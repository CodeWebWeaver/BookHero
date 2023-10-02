package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.category.CategoryDto;
import com.parkhomovsky.bookstore.dto.category.CategoryRequestDto;
import com.parkhomovsky.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toModel(CategoryRequestDto categoryRequestDto);
}
