package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.book.BookDto;
import com.parkhomovsky.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.parkhomovsky.bookstore.dto.book.BookSearchParameters;
import com.parkhomovsky.bookstore.dto.book.CreateBookRequestDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.BookMapper;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.repository.book.BookRepository;
import com.parkhomovsky.bookstore.repository.book.BookSpecificationBuilder;
import com.parkhomovsky.bookstore.service.BookService;
import java.util.List;
import java.util.Optional;
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

    @Override
    public BookDto create(CreateBookRequestDto bookRequestDto) {
        Book model = bookMapper.toEntity(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(model));
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can`t find any book with id: " + id + " during getById"));
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
    public BookDto update(Long id, CreateBookRequestDto bookRequestDto) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book updateBook = bookMapper.toEntity(bookRequestDto);
            updateBook.setId(id);
            return bookMapper.toDto(bookRepository.save(updateBook));
        }
        throw new EntityNotFoundException("Can`t find any book with id: " + id + " during update");
    }

    @Override
    public List<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getAllBooksByCategory(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
    }
}
