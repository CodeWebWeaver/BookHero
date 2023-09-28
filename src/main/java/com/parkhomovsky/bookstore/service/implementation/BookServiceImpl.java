package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.book.BookDto;
import com.parkhomovsky.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.parkhomovsky.bookstore.dto.book.BookSearchParameters;
import com.parkhomovsky.bookstore.dto.book.CreateBookRequestDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.BookMapper;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.Category;
import com.parkhomovsky.bookstore.repository.book.BookRepository;
import com.parkhomovsky.bookstore.repository.book.BookSpecificationBuilder;
import com.parkhomovsky.bookstore.repository.category.CategoryRepository;
import com.parkhomovsky.bookstore.service.BookService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto create(CreateBookRequestDto bookRequestDto) {
        Book model = bookMapper.toEntity(bookRequestDto);
        Book bookWithCategory = includeCategoryByIdInBook(model, bookRequestDto.getCategoryId());
        return bookMapper.toDto(bookRepository.save(bookWithCategory));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookRequestDto) {
        Optional<Book> optionalBook = bookRepository.findByIdWithCategory(id);
        if (optionalBook.isPresent()) {
            Book changedBook = bookMapper.toEntity(bookRequestDto);
            changedBook.setId(id);
            Book bookWithCategory =
                    includeCategoryByIdInBook(changedBook, bookRequestDto.getCategoryId());
            BookDto dto = bookMapper.toDto(bookRepository.save(bookWithCategory));
            bookMapper.setCategoryIds(dto, changedBook);
            return dto;
        }
        throw new EntityNotFoundException("Can`t find any book with id: "
                + id + " during update");
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findByIdWithCategory(id).orElseThrow(() ->
                new EntityNotFoundException("Can`t find any book with id: "
                        + id + " during getById"));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters parameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAllWithCategory(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getAllBooksByCategory(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
    }

    public Book getBookFromId(Long id) {
        Optional<Book> bookById = bookRepository.findByIdWithCategory(id);
        return bookById.orElseThrow(() ->
                new EntityNotFoundException("Can`t find any book with id: "
                + id + " during add cart item to cart"));
    }

    private Book includeCategoryByIdInBook(Book book, Set<Long> categoryIds) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categoryIds.isEmpty()) {
            throw new EntityNotFoundException("Provided categories ids not found: "
                    + categoryIds);
        }
        Set<Category> categoriesSet = new HashSet<>(categories);
        book.setCategories(categoriesSet);
        return book;
    }
}
