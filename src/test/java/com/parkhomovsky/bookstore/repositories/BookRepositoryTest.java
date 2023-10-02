package com.parkhomovsky.bookstore.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.Category;
import com.parkhomovsky.bookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class BookRepositoryTest {
    private static final Category FICTION_CATEGORY = new Category()
            .setId(1L)
            .setName("Fiction")
            .setDescription("Fiction refers to literature created from the imagination."
                    + " Mysteries, science fiction, romance, fantasy, chick lit, "
                    + " crime thrillers are all fiction genres.");
    private static final Book FICTION_BOOK = new Book()
            .setId(1L)
            .setTitle("Test Book")
            .setAuthor("Test Author")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("15"))
            .setDescription("Test book description")
            .setCoverImage("https://URL")
            .setCategories(Set.of(FICTION_CATEGORY));
    private static final Book KOBZAR_BOOK = new Book()
            .setId(3L)
            .setTitle("Kobzar")
            .setAuthor("Shevchenko")
            .setIsbn("123456789-123")
            .setPrice(new BigDecimal("15"))
            .setDescription("Good UA book")
            .setCoverImage("https://URL")
            .setCategories(Set.of(FICTION_CATEGORY));
    private static final List<Book> ALL_BOOKS = List.of(FICTION_BOOK, KOBZAR_BOOK);
    private static final Long EXPECTED_FICTION_BOOK_COUNT = 2L;
    private static final String CLEAR_BOOKS_RELATED_TABLES =
            "classpath:db-scripts/clear-books_connections-tables.sql";
    private static final String ADD_FICTION_CATEGORY =
            "classpath:db-scripts/categories/add-fiction-to-categories-table.sql";
    private static final String ADD_FICTION_BOOK =
            "classpath:db-scripts/books/add-fiction-book-to-books.sql";
    private static final String ADD_DOTA_BOOK =
            "classpath:db-scripts/books/add-dota-book-to-books.sql";
    private static final String ADD_KOBZAR_BOOK =
            "classpath:db-scripts/books/add-kobzar-book-to-books.sql";
    private static final String CREATE_FICTION_BOOK_FICTION_CONNECTION =
            "classpath:db-scripts/books_categories/add-fiction-book-fiction.sql";
    private static final String CREATE_KOBZAR_BOOK_FICTION_CONNECTION =
            "classpath:db-scripts/books_categories/add-kobzar-book-fiction.sql";

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify all returned books with creating others")
    @Sql(
            scripts = {
                    ADD_FICTION_CATEGORY,
                    CREATE_FICTION_BOOK_FICTION_CONNECTION,
                    CREATE_KOBZAR_BOOK_FICTION_CONNECTION,
                    ADD_FICTION_BOOK,
                    ADD_KOBZAR_BOOK,
                    ADD_DOTA_BOOK
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_BOOKS_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getBooksByCategory_validBookCategoryId_shouldReturnBooksDtoWithoutCategoryIds() {
        List<Book> actual = bookRepository.findAllByCategoryId(FICTION_CATEGORY.getId());
        assertEquals(EXPECTED_FICTION_BOOK_COUNT, actual.size());
        assertEquals(ALL_BOOKS, actual);
        boolean allBooksHaveCategory = actual.stream()
                .allMatch(book -> book.getCategories()
                        .stream()
                        .anyMatch(category -> category.getId().equals(FICTION_CATEGORY.getId())));
        assertTrue(allBooksHaveCategory);
    }
}
