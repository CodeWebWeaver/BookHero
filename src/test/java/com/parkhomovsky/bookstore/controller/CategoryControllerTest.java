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
import java.util.stream.IntStream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final int VALID_FANTASY_ID = 3;
    private static final int INVALID_CATEGORY_ID = 100;
    private static final CategoryDto FICTION_DTO = new CategoryDto()
            .setId(1L)
            .setName("Fiction")
            .setDescription("Fiction refers to literature created from the imagination. \n"
                    + "Mysteries, science fiction, romance, fantasy, chick lit, \n"
                    + "crime thrillers are all fiction genres.");
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
    private static final BookDtoWithoutCategoryIds RESPONSE_TEST_BOOK_DTO_WITHOUT_CATEGORY_ID =
            new BookDtoWithoutCategoryIds()
            .setId(1L)
            .setTitle("Test Book")
            .setAuthor("Test Author")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("14.56"))
            .setDescription("Test book description")
            .setCoverImage("URL");
    private static final BookDtoWithoutCategoryIds RESPONSE_KOBZAR_BOOK_DTO_WITHOUT_CATEGORY_ID =
            new BookDtoWithoutCategoryIds()
            .setId(2L)
            .setTitle("Kobzar")
            .setAuthor("Shevchenko")
            .setIsbn("123456789-123")
            .setPrice(new BigDecimal("15"))
            .setDescription("Good UA book")
            .setCoverImage("URL");

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
    @DisplayName("Test create() with a valid request body")
    void create_validRequestBody_returnCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/categories")
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
            scripts = "classpath:database/"
                    + "delete-all-from-categories-books-books_categories-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test create() with an invalid request body")
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
                    "classpath:db-scripts/categories/add-fantasy-category.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:db-scripts/categories/clear-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getAll() without pagination")
    void getAll_withoutPagination_returnListOfCategoryDtos() throws Exception {
        List<CategoryDto> expected = List.of(FICTION_DTO, COMEDY_DTO, FANTASY_DTO);
        MvcResult mvcResult = mockMvc.perform(get("/categories")).andReturn();

        List<CategoryDto> actual = List.of(objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CategoryDto[].class));

        IntStream.range(0, expected.size())
                .forEach(id -> assertTrue(EqualsBuilder
                        .reflectionEquals(expected.get(id), actual.get(id), "id")));
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    "classpath:db-scripts/categories/add-fantasy-category.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:db-scripts/categories/clear-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getCategoryById() with a correct id")
    void getCategoryById_validId_returnCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories" + VALID_FANTASY_ID)).andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(FANTASY_DTO, actual, "id"));
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    "classpath:db-scripts/categories/add-fantasy-category.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:db-scripts/categories/clear-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getCategoryById() with an invalid id")
    void getCategoryById_invalidId_throwException() throws Exception {
        mockMvc.perform(get("/categories" + INVALID_CATEGORY_ID))
                .andExpect(status().isConflict());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    "classpath:db-scripts/categories/add-fantasy-category.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:db-scripts/categories/clear-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test update() with valid id and request dto")
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
                    "classpath:db-scripts/categories/add-fantasy-category.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:db-scripts/categories/clear-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test update() with invalid id and request dto")
    void update_invalidIdAndRequestDto_throwException() throws Exception {
        mockMvc.perform(put("/categories" + INVALID_CATEGORY_ID)
                        .content(objectMapper.writeValueAsString(INVALID_FANTASY_REQUEST_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                    "classpath:db-scripts/categories/add-fantasy-category.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:db-scripts/categories/clear-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test delete() with a valid id")
    void delete_validId_returnNothing() throws Exception {
        mockMvc.perform(delete("/categories/3"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {
                    "classpath:db-scripts/categories/add-fantasy-category.sql",
                    "classpath:db-scripts/books/add-books-to-books-table.sql",
                    "classpath:db-scripts/books/books-categories-connection.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {"classpath:db-scripts/books/clear-books_connections-tables.sql",
                "classpath:db-scripts/categories/clear-categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test getBooksByCategoryId() with valid category id")
    void getBooksByCategoryId_validId_returnListOfBookDtosWithoutCategoryId() throws Exception {
        List<BookDtoWithoutCategoryIds> expected =
                List.of(RESPONSE_TEST_BOOK_DTO_WITHOUT_CATEGORY_ID,
                        RESPONSE_KOBZAR_BOOK_DTO_WITHOUT_CATEGORY_ID);
        MvcResult mvcResult = mockMvc.perform(get("/categories/1/books")).andReturn();

        List<BookDtoWithoutCategoryIds> actual = List.of(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class));

        IntStream.range(0, expected.size())
                .forEach(id -> assertTrue(EqualsBuilder
                        .reflectionEquals(expected.get(id), actual.get(id), "id")));
    }
}
