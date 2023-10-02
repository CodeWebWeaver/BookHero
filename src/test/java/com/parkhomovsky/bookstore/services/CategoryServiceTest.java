package com.parkhomovsky.bookstore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parkhomovsky.bookstore.dto.category.CategoryDto;
import com.parkhomovsky.bookstore.dto.category.CategoryRequestDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.CategoryMapper;
import com.parkhomovsky.bookstore.model.Category;
import com.parkhomovsky.bookstore.repository.category.CategoryRepository;
import com.parkhomovsky.bookstore.service.implementation.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static final Category FICTION_CATEGORY = new Category()
            .setId(1L)
            .setName("Fiction")
            .setDescription("""
                    Fiction refers to literature created from the imagination.\s
                    Mysteries, science fiction, romance, fantasy, chick lit,\s
                    crime thrillers are all fiction genres.""");
    private static final Category COMEDY_CATEGORY = new Category()
            .setId(2L)
            .setName("Comedy")
            .setDescription("Funny books that lead to make laughing of people");
    private static final Category DETECTIVE_CATEGORY = new Category()
            .setId(3L)
            .setName("Detective")
            .setDescription("""
                    Detective fiction is a subgenre of crime fiction and mystery\s
                    fiction in which an investigator or a detective—whether professional,\s
                    amateur or retired—investigates a crime, often murder""");
    private static final CategoryDto FICTION_CATEGORY_DTO = new CategoryDto()
            .setId(1L)
            .setName("Fiction")
            .setDescription("Funny books that lead to make laughing of people");
    private static final CategoryDto COMEDY_CATEGORY_DTO = new CategoryDto()
            .setId(1L)
            .setName("Comedy")
            .setDescription("Funny books that lead to make laughing of people");
    private static final CategoryDto DETECTIVE_CATEGORY_DTO = new CategoryDto()
            .setId(3L)
            .setName("Detective")
            .setDescription("""
                    Detective fiction is a subgenre of crime fiction and mystery\s
                    fiction in which an investigator or a detective—whether professional,\s
                    amateur or retired—investigates a crime, often murder""");
    private static final CategoryRequestDto DETECTIVE_CATEGORY_REQUEST_DTO =
            new CategoryRequestDto()
            .setName("Detective")
            .setDescription("""
                    Detective fiction is a subgenre of crime fiction and mystery\s
                    fiction in which an investigator or a detective—whether professional,\s
                    amateur or retired—investigates a crime, often murder""");
    private static final List<Category> CATEGORIES =
            List.of(FICTION_CATEGORY, COMEDY_CATEGORY);
    private static final List<CategoryDto> CATEGORIES_DTO =
            List.of(FICTION_CATEGORY_DTO, COMEDY_CATEGORY_DTO);
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify successful creating category with a valid input")
    public void create_validCategoryParameters_shouldReturnCategoryDto() {
        when(categoryMapper.toModel(DETECTIVE_CATEGORY_REQUEST_DTO)).thenReturn(DETECTIVE_CATEGORY);
        when(categoryRepository.save(DETECTIVE_CATEGORY)).thenReturn(DETECTIVE_CATEGORY);
        when(categoryMapper.toDto(DETECTIVE_CATEGORY)).thenReturn(DETECTIVE_CATEGORY_DTO);
        CategoryDto actual = categoryService.create(DETECTIVE_CATEGORY_REQUEST_DTO);
        assertEquals(DETECTIVE_CATEGORY_DTO, actual);
    }

    @Test
    @DisplayName("Verify returned category with category id")
    public void getById_validCategoryId_shouldReturnCategory() {
        when(categoryMapper.toDto(FICTION_CATEGORY)).thenReturn(FICTION_CATEGORY_DTO);
        when(categoryRepository.findById(FICTION_CATEGORY.getId()))
                .thenReturn(Optional.of(FICTION_CATEGORY));
        CategoryDto actualDto = categoryService.getById(FICTION_CATEGORY.getId());
        assertEquals(FICTION_CATEGORY_DTO, actualDto);
    }

    @Test
    @DisplayName("Verify all returned categories")
    public void getAll_withCategoriesInDB_shouldReturnCategoriesList() {
        when(categoryMapper.toDto(FICTION_CATEGORY)).thenReturn(FICTION_CATEGORY_DTO);
        when(categoryMapper.toDto(COMEDY_CATEGORY)).thenReturn(COMEDY_CATEGORY_DTO);
        Pageable pageable = PageRequest.of(0, 10);
        when(categoryRepository.findAll(pageable)).thenReturn(new PageImpl<>(
                CATEGORIES, pageable, CATEGORIES.size()));
        List<CategoryDto> actualDtos = categoryService.getAll(pageable);
        assertEquals(CATEGORIES_DTO, actualDtos);
    }

    @Test
    @DisplayName("Trying to find category after it deleting")
    public void deleteById_validCategoryId_shouldDeleteCategoryFromDbAndThrowExOnAccess() {
        when(categoryRepository.findById(COMEDY_CATEGORY.getId()))
                .thenReturn(Optional.empty());
        doNothing()
                .when(categoryRepository).deleteById(COMEDY_CATEGORY.getId());
        categoryService.delete(COMEDY_CATEGORY.getId());
        verify(categoryRepository).deleteById(COMEDY_CATEGORY.getId());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(COMEDY_CATEGORY.getId())
        );
        String actualMessage = exception.getMessage();
        String expectedMessage = "Can`t find any category with id: "
                + COMEDY_CATEGORY.getId()
                + " during getById";
        assertEquals(expectedMessage, actualMessage);
    }
}
