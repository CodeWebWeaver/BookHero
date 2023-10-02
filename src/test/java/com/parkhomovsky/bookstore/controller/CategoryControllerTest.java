package com.parkhomovsky.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkhomovsky.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.parkhomovsky.bookstore.dto.category.CategoryDto;
import com.parkhomovsky.bookstore.dto.category.CategoryRequestDto;
import java.math.BigDecimal;
import java.util.List;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final int VALID_FANTASY_ID = 3;
    private static final int INVALID_CATEGORY_ID = 100;
    private static final CategoryDto FICTION_DTO = new CategoryDto()
            .setId(1L)
            .setName("Fiction")
            .setDescription("Fiction books");
    private static final CategoryDto COMEDY_DTO = new CategoryDto()
            .setId(2L)
            .setName("Comedy")
            .setDescription("Funny books that lead to make laughing of people");
    private static final CategoryDto FANTASY_DTO = new CategoryDto()
            .setId(3L)
            .setName("Fantasy")
            .setDescription("Books about mystery worlds and unbelievable");
    private static final CategoryRequestDto FANTASY_REQUEST_DTO =
            new CategoryRequestDto()
                    .setName("Fantasy")
                    .setDescription("Books about mystery worlds and unbelievable");
    private static final CategoryRequestDto INVALID_FANTASY_REQUEST_DTO =
            new CategoryRequestDto()
                    .setName("")
                    .setDescription("Books about mystery worlds and unbelievable");
    private static final BookDtoWithoutCategoryIds RESPONSE_FICTION_BOOK_DTO_WITHOUT_CATEGORY_ID =
            new BookDtoWithoutCategoryIds()
                    .setId(1L)
                    .setTitle("Test Book")
                    .setAuthor("Test Author")
                    .setIsbn("1315616")
                    .setPrice(new BigDecimal("14.56"))
                    .setDescription("Test book description")
                    .setCoverImage("https://URL");
    private static final BookDtoWithoutCategoryIds RESPONSE_KOBZAR_BOOK_DTO_WITHOUT_CATEGORY_ID =
            new BookDtoWithoutCategoryIds()
            .setId(3L)
            .setTitle("Kobzar")
            .setAuthor("Shevchenko")
            .setIsbn("123456789-123")
            .setPrice(new BigDecimal("15"))
            .setDescription("Good UA book")
            .setCoverImage("https://URL");
    private static final String CLEAR_BOOKS_RELATED_TABLES =
            "classpath:db-scripts/clear-books_connections-tables.sql";

    private static final String ADD_COMEDY_CATEGORY =
            "classpath:db-scripts/categories/add-comedy-to-categories-table.sql";
    private static final String ADD_FICTION_CATEGORY =
            "classpath:db-scripts/categories/add-fiction-to-categories-table.sql";
    private static final String ADD_FANTASY_CATEGORY =
            "classpath:db-scripts/categories/add-fantasy-to-categories-table.sql";
    private static final String ADD_FICTION_BOOK =
            "classpath:db-scripts/books/add-fiction-book-to-books.sql";
    private static final String ADD_DOTA_BOOK =
            "classpath:db-scripts/books/add-dota-book-to-books.sql";
    private static final String ADD_KOBZAR_BOOK =
            "classpath:db-scripts/books/add-kobzar-book-to-books.sql";
    private static final String CLEAR_CATEGORIES_TABLE =
            "classpath:db-scripts/categories/clear-categories-table.sql";
    private static final String CREATE_FICTION_BOOK_FICTION_CONNECTION =
            "classpath:db-scripts/books_categories/add-fiction-book-fiction.sql";
    private static final String CREATE_KOBZAR_BOOK_FICTION_CONNECTION =
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
            scripts = CLEAR_BOOKS_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test creating category with a valid request body")
    void create_validRequestBody_returnCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(FANTASY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(FANTASY_DTO, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = CLEAR_BOOKS_RELATED_TABLES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test creating category with an invalid request body")
    void create_invalidRequestBody_throwException() throws Exception {
        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(INVALID_FANTASY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    ADD_FICTION_CATEGORY,
                    ADD_COMEDY_CATEGORY,
                    ADD_FANTASY_CATEGORY
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_CATEGORIES_TABLE,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get all categories from db")
    void getAll_withoutPagination_returnListOfCategoryDtos() throws Exception {
        List<CategoryDto> expected = List.of(FICTION_DTO, COMEDY_DTO, FANTASY_DTO);
        MvcResult mvcResult = mockMvc.perform(get("/categories")).andReturn();

        List<CategoryDto> actual = List.of(objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto[].class));

        boolean allExpectedBooksFound = expected.stream()
                .allMatch(expectedBook -> actual.stream()
                        .anyMatch(actualBook -> expectedBook.getId().equals(actualBook.getId())));

        assertTrue(allExpectedBooksFound, "Not all expected books were found in actual list.");
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = ADD_FANTASY_CATEGORY,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_CATEGORIES_TABLE,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get category by id")
    void getCategoryById_validId_returnCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories/" + VALID_FANTASY_ID)).andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(FANTASY_DTO, actual, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = ADD_FANTASY_CATEGORY,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_CATEGORIES_TABLE,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test get category by invalid id")
    void getCategoryById_invalidId_throwException() throws Exception {
        mockMvc.perform(get("/categories" + INVALID_CATEGORY_ID))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_FICTION_CATEGORY,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_CATEGORIES_TABLE,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test updating category with valid id and request dto")
    void update_validIdAndRequestDto_returnCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/categories/1")
                        .content(objectMapper.writeValueAsString(FANTASY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(FANTASY_DTO, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    ADD_FANTASY_CATEGORY
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_CATEGORIES_TABLE,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test update() with invalid id and request dto")
    void update_invalidIdAndRequestDto_throwException() throws Exception {
        mockMvc.perform(put("/categories" + INVALID_CATEGORY_ID)
                        .content(objectMapper.writeValueAsString(INVALID_FANTASY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    ADD_FANTASY_CATEGORY
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = CLEAR_CATEGORIES_TABLE,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test delete() with a valid id")
    void delete_validId_returnNothing() throws Exception {
        mockMvc.perform(delete("/categories/3"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user")
    @Test
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
    @DisplayName("Test getting books by valid category id")
    void getBooksByCategoryId_validId_returnListOfBookDtosWithoutCategoryId() throws Exception {
        List<BookDtoWithoutCategoryIds> expected =
                List.of(RESPONSE_FICTION_BOOK_DTO_WITHOUT_CATEGORY_ID,
                        RESPONSE_KOBZAR_BOOK_DTO_WITHOUT_CATEGORY_ID);
        MvcResult mvcResult = mockMvc.perform(get("/categories/1/books")).andReturn();

        List<BookDtoWithoutCategoryIds> actual = List.of(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class));

        boolean allExpectedBooksFound = expected.stream()
                .allMatch(expectedBook -> actual.stream()
                        .anyMatch(actualBook -> expectedBook.getId().equals(actualBook.getId())));

        assertTrue(allExpectedBooksFound, "Not all expected books were found in actual list.");
    }
}
