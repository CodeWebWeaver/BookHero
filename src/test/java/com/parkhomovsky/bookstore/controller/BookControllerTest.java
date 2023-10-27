package com.parkhomovsky.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkhomovsky.bookstore.dto.book.BookDto;
import com.parkhomovsky.bookstore.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final int VALID_BOOK_ID = 1;
    private static final int INVALID_BOOK_ID = 151;
    private static final BookDto RESPONSE_FICTION_BOOK_DTO = new BookDto()
            .setId(1L)
            .setTitle("Test Book")
            .setAuthor("Test Author")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("15"))
            .setDescription("Test book description")
            .setCoverImage("https://URL")
            .setCategoryIds(Set.of(1L));
    private static final BookDto RESPONSE_DOTA_BOOK_DTO = new BookDto()
            .setId(2L)
            .setTitle("Dota 2: Pain and Casino")
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("https://resources/bookImages/Dota-Pain.png")
            .setCategoryIds(Set.of(2L));
    private static final BookDto RESPONSE_CREATED_DOTA_BOOK_DTO = new BookDto()
            .setId(1L)
            .setTitle("Dota 2: Pain and Casino")
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("https://resources/bookImages/Dota-Pain.png")
            .setCategoryIds(Set.of(2L));

    private static final BookDto KOBZAR_BOOK_DTO = new BookDto()
            .setId(3L)
            .setTitle("Kobzar")
            .setAuthor("Shevchenko")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("15"))
            .setDescription("Good UA book")
            .setCoverImage("https://URL")
            .setCategoryIds(Set.of(1L));
    private static final CreateBookRequestDto CREATE_BOOK_REQUEST_DTO =
            new CreateBookRequestDto()
            .setTitle("Dota 2: Pain and Casino")
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("https://resources/bookImages/Dota-Pain.png")
            .setCategoryId(Set.of(2L));
    private static final CreateBookRequestDto INVALID_CREATE_BOOK_REQUEST_DTO =
            new CreateBookRequestDto()
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("resources/bookImages/Dota-Pain.png")
            .setCategoryId(Set.of(2L));
    private static final String ADD_COMEDY_CATEGORY =
            "classpath:db-scripts/categories/add-comedy-to-categories-table.sql";
    private static final String ADD_FICTION_CATEGORY =
            "classpath:db-scripts/categories/add-fiction-to-categories-table.sql";
    private static final String ADD_FANTASY_CATEGORY =
            "classpath:db-scripts/categories/add-fantasy-to-categories-table.sql";
    private static final String CLEAR_BOOK_CATEGORIES_TABLES =
            "classpath:db-scripts/clear-books_connections-tables.sql";
    private static final String ADD_FICTION_BOOK =
            "classpath:db-scripts/books/add-fiction-book-to-books.sql";
    private static final String ADD_DOTA_BOOK =
            "classpath:db-scripts/books/add-dota-book-to-books.sql";
    private static final String ADD_KOBZAR_BOOK =
            "classpath:db-scripts/books/add-kobzar-book-to-books.sql";
    private static final String CONNECT_FICTION_BOOK_AND_FICTION_CATEGORY =
            "classpath:db-scripts/books_categories/add-fiction-book-fiction.sql";
    private static final String CONNECT_DOTA_BOOK_AND_COMEDY_CATEGORY =
            "classpath:db-scripts/books_categories/add-dota-book-comedy.sql";
    private static final String CONNECT_KOBZAR_BOOK_AND_FICTION_CATEGORY =
            "classpath:db-scripts/books_categories/add-kobzar-book-fiction.sql";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_COMEDY_CATEGORY,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_BOOK_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Creating book with valid book params. Ok Status and DTO Expected")
    void create_withValidBook_returnCreatedBookDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(CREATE_BOOK_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_CREATED_DOTA_BOOK_DTO, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:db-scripts/categories/add-fiction-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_BOOK_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create book with absent book title. Bad Request expected")
    void create_withInvalidBook_throwException() throws Exception {
        mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(INVALID_CREATE_BOOK_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION_BOOK,
                    ADD_DOTA_BOOK,
                    ADD_KOBZAR_BOOK,
                    ADD_FICTION_CATEGORY,
                    ADD_COMEDY_CATEGORY,
                    ADD_FANTASY_CATEGORY,
                    CONNECT_FICTION_BOOK_AND_FICTION_CATEGORY,
                    CONNECT_DOTA_BOOK_AND_COMEDY_CATEGORY,
                    CONNECT_KOBZAR_BOOK_AND_FICTION_CATEGORY
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_BOOK_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Testing get all books from db")
    void findAll_withoutPagination_returnListOfBookDtos() throws Exception {
        List<BookDto> expected =
                List.of(RESPONSE_FICTION_BOOK_DTO, RESPONSE_DOTA_BOOK_DTO, KOBZAR_BOOK_DTO);
        MvcResult mvcResult = mockMvc.perform(get("/books")).andReturn();
        List<BookDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto[].class));
        boolean allExpectedBooksFound = expected.stream()
                .allMatch(expectedBook -> actual.stream()
                        .anyMatch(actualBook -> expectedBook.getId().equals(actualBook.getId())));

        assertTrue(allExpectedBooksFound, "Not all expected books were found in actual list.");
    }

    //@WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION_BOOK,
                    CONNECT_FICTION_BOOK_AND_FICTION_CATEGORY,
                    ADD_FICTION_CATEGORY
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_BOOK_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getById with a correct id")
    void getById_validId_returnBookDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books/" + VALID_BOOK_ID)).andReturn();
        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_FICTION_BOOK_DTO, actual, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION_BOOK,
                    CONNECT_FICTION_BOOK_AND_FICTION_CATEGORY,
                    ADD_FICTION_CATEGORY
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_BOOK_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getById with an invalid id")
    void getById_invalidId_throwException() throws Exception {
        mockMvc.perform(get("/books" + INVALID_BOOK_ID))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION_BOOK,
                    CONNECT_FICTION_BOOK_AND_FICTION_CATEGORY,
                    ADD_FICTION_CATEGORY
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_BOOK_CATEGORIES_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test deleteById() with a valid id")
    void deleteById_validId_returnNothing() throws Exception {
        mockMvc.perform(delete("/books/" + VALID_BOOK_ID))
                .andExpect(status().isNoContent());
    }
}
