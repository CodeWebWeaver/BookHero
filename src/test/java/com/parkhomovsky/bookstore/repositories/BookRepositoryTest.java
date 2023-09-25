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
            .setName("Fiction");
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
    private static final Book EXIST_TEST_BOOK = new Book()
            .setTitle("Test Book")
            .setAuthor("Test Author")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("14.56"))
            .setDescription("Test book description")
            .setCoverImage("URL")
            .setCategories(Set.of(FICTION_CATEGORY));
    private static final Long ABSENT_BOOK_ID = 128L;
    private static final List<Book> ALL_BOOKS = List.of(EXIST_TEST_BOOK, CREATED_DOTA_BOOK);
    private static final Long EXPECTED_FICTION_BOOK_COUNT = 1L;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify all returned books with creating another one")
    @Sql(scripts = {
            "classpath:database/books/add-kobzar-book-to-books-table.sql" },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-kobzar-book-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategory_validBookCategoryId_shouldReturnBooksDtoWithoutCategoryIds() {
        List<Book> actual = bookRepository.findAllByCategoryId(FICTION_CATEGORY.getId());
        assertEquals(EXPECTED_FICTION_BOOK_COUNT, actual.size());
        assertEquals(ALL_BOOKS, actual);
        boolean isRequestedCategory = actual.stream()
                .allMatch(book -> book.getCategories().contains(FICTION_CATEGORY));
        assertTrue(isRequestedCategory);
    }
}
