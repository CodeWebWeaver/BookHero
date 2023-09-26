package com.parkhomovsky.bookstore.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.enums.RoleName;
import com.parkhomovsky.bookstore.mapper.CartItemMapper;
import com.parkhomovsky.bookstore.mapper.ShoppingCartMapper;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.Category;
import com.parkhomovsky.bookstore.model.Role;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.cart.ShoppingCartRepository;
import com.parkhomovsky.bookstore.repository.item.CartItemRepository;
import com.parkhomovsky.bookstore.service.implementation.ShoppingCartServiceImpl;
import com.parkhomovsky.bookstore.service.implementation.UserServiceImpl;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartServiceTest {

    private static final Role ROLE_USER = new Role()
            .setId(1L)
            .setName(RoleName.ROLE_USER);
    private static final User USER = new User()
            .setId(1L)
            .setEmail("user@example.com")
            .setPassword("123456789")
            .setFirstName("User")
            .setLastName("Userovich")
            .setShippingAddress("Shevchenka 43")
            .setRoles(Set.of(ROLE_USER));
    private static final Category FICTION = new Category()
            .setId(1L)
            .setName("Fiction")
            .setDescription("Fiction books");
    private static final Category COMEDY = new Category()
            .setId(2L)
            .setName("Comedy")
            .setDescription("Funny books that lead to make laughing of people");
    private static final Category FANTASY = new Category()
            .setId(3L)
            .setName("Fantasy")
            .setDescription("Books about mystery worlds and unbelievable");

    private static final Book FICTION_BOOK = new Book()
            .setId(1L)
            .setTitle("Test Book")
            .setAuthor("Test Author")
            .setIsbn("1315616")
            .setPrice(new BigDecimal("15"))
            .setDescription("Test book description")
            .setCoverImage("https://URL")
            .setCategories(Set.of(FICTION));
    private static final Book DOTA_BOOK = new Book()
            .setId(2L)
            .setTitle("Dota 2: Pain and Casino")
            .setAuthor("U.N. LoLovich")
            .setIsbn("978-0-9767736-6-5")
            .setPrice(new BigDecimal("25"))
            .setDescription("Book about why you shoudn`t play Dota 2."
                    + " And why you can`t still find a girl.")
            .setCoverImage("https://resources/bookImages/Dota-Pain.png")
            .setCategories(Set.of(COMEDY));
    private static final Book KOBZAR_BOOK = new Book()
            .setId(3L)
            .setTitle("Kobzar")
            .setAuthor("Shevchenko")
            .setIsbn("123456789-123")
            .setPrice(new BigDecimal("15"))
            .setDescription("Good UA book")
            .setCoverImage("https://URL")
            .setCategories(Set.of(FICTION));
    private static final CartItem FICTION_CART_ITEM = new CartItem()
            .setId(1L)
            .setBook(FICTION_BOOK)
            .setShoppingCart(new ShoppingCart())
            .setQuantity(1);
    private static final CartItem DOTA_CART_ITEM = new CartItem()
            .setId(2L)
            .setBook(DOTA_BOOK)
            .setShoppingCart(new ShoppingCart())
            .setQuantity(1);

    private static final CartItem KOBZAR_CART_ITEM = new CartItem()
            .setId(3L)
            .setBook(KOBZAR_BOOK)
            .setShoppingCart(new ShoppingCart())
            .setQuantity(2);
    private static final ShoppingCart SHOPPING_CART = new ShoppingCart()
            .setId(1L)
            .setUser(USER)
            .setCartItems(Set.of(FICTION_CART_ITEM, DOTA_CART_ITEM));
    private static final CartItemDto FICTION_CART_ITEM_DTO = new CartItemDto()
            .setId(1L)
            .setBookId(1L)
            .setBookTitle("Fiction")
            .setQuantity(2);
    private static final CartItemDto DOTA_CART_ITEM_DTO = new CartItemDto()
            .setId(2L)
            .setBookId(2L)
            .setBookTitle("Dota 2: Pain and Casino")
            .setQuantity(1);
    private static final CartItemDto KOBZAR_CART_ITEM_DTO = new CartItemDto()
            .setId(3L)
            .setBookId(3L)
            .setBookTitle("Kobzar")
            .setQuantity(2);

    private static final ShoppingCartDto SHOPPING_CART_DTO = new ShoppingCartDto()
            .setId(1L)
            .setUserId(1L)
            .setCartItemDtos(Set.of(FICTION_CART_ITEM_DTO, DOTA_CART_ITEM_DTO));
    private static final ShoppingCartDto CREATED_SHOPPING_CART_DTO = new ShoppingCartDto()
            .setId(1L)
            .setUserId(1L)
            .setCartItemDtos(new HashSet<>());
    private static final ShoppingCart CREATED_SHOPPING_CART = new ShoppingCart()
            .setUser(USER)
            .setCartItems(new HashSet<>());
    private static final ShoppingCart SAVED_SHOPPING_CART = new ShoppingCart()
            .setId(1L)
            .setUser(USER)
            .setCartItems(new HashSet<>());
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final Long FICTION_BOOK_ID = 1L;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartServiceImpl;
    @BeforeAll
    static void beforeAll() {
        FICTION_CART_ITEM.setShoppingCart(SHOPPING_CART);
        DOTA_CART_ITEM.setShoppingCart(SHOPPING_CART);
        KOBZAR_CART_ITEM.setShoppingCart(SHOPPING_CART);
    }
    @WithMockUser(username = "user")
    @Test
    void getUserShoppingCart_existsShoppingCart_shouldReturnShoppingCartDto() {
        mockgetAuthenticatedUser();
        when(cartItemMapper.toDto(FICTION_CART_ITEM)).thenReturn(FICTION_CART_ITEM_DTO);
        when(cartItemMapper.toDto(DOTA_CART_ITEM)).thenReturn(DOTA_CART_ITEM_DTO);
        when(cartItemMapper.toDto(KOBZAR_CART_ITEM)).thenReturn(KOBZAR_CART_ITEM_DTO);
        when(shoppingCartRepository.findByUserId(any())).thenReturn(
                Optional.of(SHOPPING_CART));
        when(shoppingCartMapper.toDto(any())).thenReturn(SHOPPING_CART_DTO);

        ShoppingCartDto actual = shoppingCartServiceImpl.getUserShoppingCartDto();
        assertEquals(SHOPPING_CART_DTO, actual);
    }

    @WithMockUser(username = "user")
    @Test
    void getUserShoppingCart_nonExistsShoppingCart_shouldReturnShoppingCartDto() {
        mockgetAuthenticatedUser();
        when(shoppingCartRepository.findByUserId(any())).thenReturn(
                Optional.empty());
        when(shoppingCartRepository.save(CREATED_SHOPPING_CART)).thenReturn(SAVED_SHOPPING_CART);
        when(shoppingCartMapper.toDto(any())).thenReturn(CREATED_SHOPPING_CART_DTO);

        ShoppingCartDto actual = shoppingCartServiceImpl.getUserShoppingCartDto();
        assertEquals(CREATED_SHOPPING_CART_DTO, actual);
    }

    @WithMockUser(username = "user")
    @Test
    void clearShoppingCart_ExistsShoppingCart_ShouldDeleteCartItems() {
        mockgetAuthenticatedUser();
        when(shoppingCartRepository.findByUserId(any())).thenReturn(Optional.of(SHOPPING_CART));

        shoppingCartServiceImpl.clearShoppingCart();

        assertDoesNotThrow(() ->
                cartItemRepository.deleteAllByShoppingCartId(SHOPPING_CART.getId()));
    }

    private void mockgetAuthenticatedUser() {
        when(userService.getAuthenticatedUser()).thenReturn(USER);
    }
}
