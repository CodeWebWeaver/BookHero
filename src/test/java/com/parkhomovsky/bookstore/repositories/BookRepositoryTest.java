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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final Category COMEDY_CATEGORY = new Category()
            .setId(2L)
            .setName("Comedy");
    private static final Category FICTION_CATEGORY = new Category()
            .setId(1L)
            .setName("Fiction")
            .setDescription("Fiction refers to literature created from the imagination."
                    + " Mysteries, science fiction, romance, fantasy, chick lit, "
                    + " crime thrillers are all fiction genres.");
    private static final Book DOTA_BOOK = new Book()
            .setId(2L)
            .setTitle("Dota 2: Pain and Casino")
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("resources/bookImages/Dota-Pain.png")
            .setDeleted(false)
            .setCategories(Set.of(COMEDY_CATEGORY));
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

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify all returned books with creating others")
    @Sql(
            scripts = {
                    "classpath:db-scripts/categories/add-fiction-to-categories-table.sql",
                    "classpath:db-scripts/books_categories/add-fiction-book-fiction.sql",
                    "classpath:db-scripts/books_categories/add-kobzar-book-fiction.sql",
                    "classpath:db-scripts/books/add-fiction-book-to-books.sql",
                    "classpath:db-scripts/books/add-kobzar-book-to-books.sql",
                    "classpath:db-scripts/books/add-dota-book-to-books.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {"classpath:db-scripts/clear-books_connections-tables.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getBooksByCategory_validBookCategoryId_shouldReturnBooksDtoWithoutCategoryIds() {
        List<Book> actual = bookRepository.findAllByCategoryId(FICTION_CATEGORY.getId());
        assertEquals(EXPECTED_FICTION_BOOK_COUNT, actual.size());
        assertEquals(ALL_BOOKS, actual);
        boolean allBooksHaveCategory = actual.stream()
                .allMatch(book -> book.getCategories()
                        .stream()
                        .anyMatch(category -> category.getId() == FICTION_CATEGORY.getId()));
        assertTrue(allBooksHaveCategory);
    }
}
