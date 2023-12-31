package com.parkhomovsky.bookstore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.enums.RoleName;
import com.parkhomovsky.bookstore.mapper.CartItemMapper;
import com.parkhomovsky.bookstore.mapper.ShoppingCartMapper;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.Category;
import com.parkhomovsky.bookstore.model.Role;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.item.CartItemRepository;
import com.parkhomovsky.bookstore.service.BookService;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import com.parkhomovsky.bookstore.service.implementation.CartItemServiceImpl;
import com.parkhomovsky.bookstore.service.implementation.UserServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
public class CartItemsServiceTest {
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
    private static final ShoppingCart SHOPPING_CART = new ShoppingCart()
            .setId(1L)
            .setUser(USER)
            .setCartItems(Set.of(FICTION_CART_ITEM, DOTA_CART_ITEM));

    private static final AddCartItemRequestDto ADD_CART_ITEM_REQUEST_DTO =
            new AddCartItemRequestDto()
            .setQuantity(2);
    private static final List<CartItem> SHOPPING_CART_ITEM_LIST =
            List.of(FICTION_CART_ITEM, DOTA_CART_ITEM);

    private static final CartItem UPDATED_FICTION_CART_ITEM = FICTION_CART_ITEM
            .setQuantity(FICTION_CART_ITEM.getQuantity()
                    + ADD_CART_ITEM_REQUEST_DTO.getQuantity());
    private static final CartItemDto UPDATED_FICTION_CART_ITEM_DTO = new CartItemDto()
            .setId(1L)
            .setBookId(1L)
            .setBookTitle("Test Book")
            .setQuantity(3);

    private static final ShoppingCart EMPTY_SHOPPING_CART = new ShoppingCart()
            .setId(1L)
            .setUser(USER)
            .setCartItems(new HashSet<>());
    private static final ShoppingCart SHOPPING_CART_WITH_NEW_CART_ITEM = new ShoppingCart()
            .setId(1L)
            .setUser(USER)
            .setCartItems(Set.of(FICTION_CART_ITEM));
    private static final CreateCartItemRequestDto CREATE_CART_ITEM_REQUEST_DTO =
            new CreateCartItemRequestDto()
                    .setBookId(1L)
                    .setQuantity(1);
    private static final CartItem NEW_CART_ITEM =
            new CartItem()
                    .setId(1L)
                    .setQuantity(1);
    private static final CartItem INITIALIZED_NEW_CART_ITEM = NEW_CART_ITEM
            .setBook(FICTION_BOOK)
            .setShoppingCart(SHOPPING_CART_WITH_NEW_CART_ITEM);

    private static final CartItemDto NEW_CART_ITEM_DTO =
            new CartItemDto()
                    .setId(1L)
                    .setQuantity(1)
                    .setBookId(FICTION_BOOK.getId())
                    .setBookTitle(FICTION_BOOK.getTitle());

    private static final List<CartItem> EMPTY_SHOPPING_CART_ITEM_LIST =
            new ArrayList<>();
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private BookService bookService;
    @Mock
    private ShoppingCartService shoppingCartService;
    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @WithMockUser(username = "user")
    @Test
    void add_withValidCartItemIdAndQuantity_shouldReturnDtoAfterAdding() {
        when(shoppingCartService.getShoppingCart()).thenReturn(SHOPPING_CART);
        when(cartItemRepository.findAllByShoppingCartId(SHOPPING_CART.getId()))
                .thenReturn(SHOPPING_CART_ITEM_LIST);
        when(cartItemRepository.save(UPDATED_FICTION_CART_ITEM))
                .thenReturn(UPDATED_FICTION_CART_ITEM);
        when(cartItemMapper.toDto(UPDATED_FICTION_CART_ITEM))
                .thenReturn(UPDATED_FICTION_CART_ITEM_DTO);

        CartItemDto actual = cartItemService.add(1L, ADD_CART_ITEM_REQUEST_DTO);
        assertEquals(UPDATED_FICTION_CART_ITEM_DTO, actual);
    }

    @WithMockUser(username = "user")
    @Test
    void create_withValidCreateCartItemDtoAndEmptyShoppingCart_shouldReturnDtoCreating() {
        when(shoppingCartService.getShoppingCart()).thenReturn(EMPTY_SHOPPING_CART);
        when(cartItemMapper.toModel(CREATE_CART_ITEM_REQUEST_DTO)).thenReturn(NEW_CART_ITEM);
        when(bookService.getBookFromId(CREATE_CART_ITEM_REQUEST_DTO
                .getBookId())).thenReturn(FICTION_BOOK);
        when(cartItemRepository.findAllByShoppingCartId(SHOPPING_CART.getId()))
                .thenReturn(EMPTY_SHOPPING_CART_ITEM_LIST);
        when(cartItemRepository.save(INITIALIZED_NEW_CART_ITEM))
                .thenReturn(INITIALIZED_NEW_CART_ITEM);
        when(cartItemMapper.toDto(INITIALIZED_NEW_CART_ITEM)).thenReturn(NEW_CART_ITEM_DTO);

        CartItemDto actual = cartItemService.create(CREATE_CART_ITEM_REQUEST_DTO);
        assertEquals(NEW_CART_ITEM_DTO, actual);
    }
}
