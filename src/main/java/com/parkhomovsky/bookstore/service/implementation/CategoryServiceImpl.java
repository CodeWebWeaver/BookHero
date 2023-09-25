package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.category.CategoryDto;
import com.parkhomovsky.bookstore.dto.category.CategoryRequestDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.CategoryMapper;
import com.parkhomovsky.bookstore.model.Category;
import com.parkhomovsky.bookstore.repository.category.CategoryRepository;
import com.parkhomovsky.bookstore.service.CategoryService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto create(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.toModel(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto categoryRequestDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category updateCategory = categoryMapper.toModel(categoryRequestDto);
            updateCategory.setId(id);
            return categoryMapper.toDto(categoryRepository.save(updateCategory));
        }
        throw new EntityNotFoundException("Can`t find any category with id: "
                + id + " during update");
    }

    @Override
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            return categoryMapper.toDto(optionalCategory.get());
        }
        throw new EntityNotFoundException("Can`t find any category with id: "
                + id + " during getById");
    }
}
