package com.parkhomovsky.bookstore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parkhomovsky.bookstore.dto.book.BookDto;
import com.parkhomovsky.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.parkhomovsky.bookstore.dto.book.CreateBookRequestDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.BookMapper;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.Category;
import com.parkhomovsky.bookstore.repository.book.BookRepository;
import com.parkhomovsky.bookstore.service.implementation.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
class BookServiceTest {
    private static final Category COMEDY_CATEGORY = new Category()
            .setId(2L)
            .setName("Comedy");
    private static final Category FICTION_CATEGORY = new Category()
            .setId(1L)
            .setName("Fiction");
    private static final CreateBookRequestDto REQUEST_CREATE_DOTA_BOOK_DTO =
            new CreateBookRequestDto()
            .setTitle("Dota 2: Pain and Casino")
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("resources/bookImages/Dota-Pain.png")
            .setCategoryId(Set.of(2L));
    private static final Book CREATED_DOTA_BOOK = new Book()
            .setId(2L)
            .setTitle("Dota 2: Pain and Casino")
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("resources/bookImages/Dota-Pain.png")
            .setCategories(Set.of(COMEDY_CATEGORY));
    private static final BookDto RESPONSE_CREATED_DOTA_BOOK_DTO = new BookDto()
            .setId(2L)
            .setTitle("Dota 2: Pain and Casino")
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("resources/bookImages/Dota-Pain.png")
            .setCategoryIds(Set.of(2L));
    private static final Book EXIST_TEST_BOOK = new Book()
            .setTitle("Test Book")
            .setAuthor("Test Author")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("14.56"))
            .setDescription("Test book description")
            .setCoverImage("URL")
            .setCategories(Set.of(FICTION_CATEGORY));
    private static final BookDto RESPONSE_EXIST_TEST_BOOK_DTO = new BookDto()
            .setId(1L)
            .setTitle("Test Book")
            .setAuthor("Test Author")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("14.56"))
            .setDescription("Test book description")
            .setCoverImage("URL")
            .setCategoryIds(Set.of(1L));
    private static final BookDtoWithoutCategoryIds
            RESPONSE_EXIST_TEST_BOOK_DTO_WITHOUT_CATEGORIES = new BookDtoWithoutCategoryIds()
            .setId(1L)
            .setTitle("Test Book")
            .setAuthor("Test Author")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("14.56"))
            .setDescription("Test book description")
            .setCoverImage("URL");
    private static final List<BookDtoWithoutCategoryIds> ALL_FICTION_BOOKS_DTO
            = List.of(RESPONSE_EXIST_TEST_BOOK_DTO_WITHOUT_CATEGORIES);
    private static final Long ABSENT_BOOK_ID = 128L;

    private static final List<BookDto> ALL_BOOKS_DTO_RESPONSE =
            List.of(RESPONSE_EXIST_TEST_BOOK_DTO, RESPONSE_CREATED_DOTA_BOOK_DTO);
    private static final List<Book> ALL_BOOKS = List.of(EXIST_TEST_BOOK, CREATED_DOTA_BOOK);
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify successful creating book with a valid input")
    public void create_validBookParameters_shouldReturnBookDto() {
        when(bookMapper.toEntity(REQUEST_CREATE_DOTA_BOOK_DTO)).thenReturn(CREATED_DOTA_BOOK);
        when(bookRepository.save(CREATED_DOTA_BOOK)).thenReturn(CREATED_DOTA_BOOK);
        when(bookMapper.toDto(CREATED_DOTA_BOOK)).thenReturn(RESPONSE_CREATED_DOTA_BOOK_DTO);

        BookDto actual = bookService.create(REQUEST_CREATE_DOTA_BOOK_DTO);
        assertEquals(RESPONSE_CREATED_DOTA_BOOK_DTO, actual);
    }

    @Test
    @DisplayName("Verify returned book with a valid input id")
    public void findById_validBookId_shouldReturnBookDto() {
        when(bookMapper.toDto(EXIST_TEST_BOOK))
                .thenReturn(RESPONSE_EXIST_TEST_BOOK_DTO);
        when(bookRepository.findById(EXIST_TEST_BOOK.getId()))
                .thenReturn(Optional.of(EXIST_TEST_BOOK));
        BookDto actual = bookService.getById(EXIST_TEST_BOOK.getId());
        assertEquals(RESPONSE_EXIST_TEST_BOOK_DTO, actual);
    }

    @Test
    @DisplayName("Verify returned book with a wrong input id")
    public void findById_invalidBookId_shouldThrowEntityNotFoundException() {
        when(bookRepository.findById(ABSENT_BOOK_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getById(ABSENT_BOOK_ID)
                );
        String expectedMessage = "Can`t find any book with id: "
                + ABSENT_BOOK_ID
                + " during getById";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Verify all returned books with creating another one")
    public void getAll_validBookParams_shouldReturnListBookDto() {
        when(bookMapper.toEntity(REQUEST_CREATE_DOTA_BOOK_DTO)).thenReturn(CREATED_DOTA_BOOK);
        when(bookRepository.save(CREATED_DOTA_BOOK)).thenReturn(CREATED_DOTA_BOOK);
        when(bookMapper.toDto(CREATED_DOTA_BOOK)).thenReturn(RESPONSE_CREATED_DOTA_BOOK_DTO);
        when(bookMapper.toDto(EXIST_TEST_BOOK)).thenReturn(RESPONSE_EXIST_TEST_BOOK_DTO);
        bookService.create(REQUEST_CREATE_DOTA_BOOK_DTO);
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(
                ALL_BOOKS, pageable, ALL_BOOKS.size()));
        List<BookDto> actual = bookService.getAll(pageable);
        assertEquals(ALL_BOOKS_DTO_RESPONSE, actual);
    }

    @Test
    @DisplayName("Verify all returned books by specific category id")
    public void getBooksByCategory_validBookCategoryId_shouldReturnBooksDtoWithoutCategoryIds() {
        when(bookMapper.toDtoWithoutCategories(EXIST_TEST_BOOK))
                .thenReturn(RESPONSE_EXIST_TEST_BOOK_DTO_WITHOUT_CATEGORIES);
        when(bookRepository.findAllByCategoryId(FICTION_CATEGORY
                .getId())).thenReturn(List.of(EXIST_TEST_BOOK));
        List<BookDtoWithoutCategoryIds> actual =
                bookService.getAllBooksByCategory(FICTION_CATEGORY.getId());
        assertEquals(ALL_FICTION_BOOKS_DTO, actual);
    }

    @Test
    @DisplayName("Trying to find book after it deleting")
    public void deleteById_validBookId_shouldDeleteBookFromDbAndThrowExOnAccess() {
        when(bookRepository.findById(EXIST_TEST_BOOK.getId()))
                .thenReturn(Optional.empty());
        doNothing()
                .when(bookRepository).deleteById(EXIST_TEST_BOOK.getId());
        bookService.deleteById(EXIST_TEST_BOOK.getId());
        verify(bookRepository).deleteById(EXIST_TEST_BOOK.getId());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getById(EXIST_TEST_BOOK.getId())
        );
        String actualMessage = exception.getMessage();
        String expectedMessage = "Can`t find any book with id: "
                + EXIST_TEST_BOOK.getId()
                + " during getById";
        assertEquals(expectedMessage, actualMessage);
    }

}
