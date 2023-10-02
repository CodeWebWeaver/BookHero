package com.parkhomovsky.bookstore.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.parkhomovsky.bookstore.enums.RoleName;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.Category;
import com.parkhomovsky.bookstore.model.Role;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.cart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
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
public class ShoppingCartRepositoryTest {
    private static final Role ROLE_USER = new Role()
            .setId(1L)
            .setName(RoleName.ROLE_USER);
    private static final User USER = new User()
            .setId(1L)
            .setEmail("wylo@ua.com")
            .setPassword("123456789")
            .setFirstName("Oleh")
            .setLastName("Lyashko")
            .setShippingAddress("N1 in ballot paper")
            .setRoles(Set.of(ROLE_USER))
            .setIsDeleted(false);
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
    private static final Set<CartItem> SHOPPING_CART_ITEM_SET =
            Set.of(FICTION_CART_ITEM, DOTA_CART_ITEM);

    private static final ShoppingCart EMPTY_SHOPPING_CART = new ShoppingCart()
            .setId(1L)
            .setUser(USER)
            .setCartItems(new HashSet<>());
    private static final String ADD_FICTION_BOOK =
            "classpath:db-scripts/books/add-fiction-book-to-books.sql";
    private static final String ADD_DOTA_BOOK =
            "classpath:db-scripts/books/add-dota-book-to-books.sql";
    private static final String ADD_USER_SHOPPING_CART =
            "classpath:db-scripts/shopping-carts/add-user-shopping-cart-to-shopping-carts.sql";
    private static final String ADD_DOTA_CART_ITEM =
            "classpath:db-scripts/cart-items/add-dota-cart-item-to-cart-items.sql";
    private static final String ADD_FICTION_CART_ITEM =
            "classpath:db-scripts/cart-items/add-fiction-cart-item-to-cart-items.sql";

    private static final String CLEAR_CART_ITEMS_TABLE =
            "classpath:db-scripts/cart-items/clear-cart-items-table.sql";
    private static final String CLEAR_SHOPPING_CARTS_TABLE =
            "classpath:db-scripts/shopping-carts/clear-shopping-cart-table.sql";

    private static final String ADD_USER_TO_TABLE =
            "classpath:db-scripts/users/add-user-to-users-table.sql";

    private static final String CLEAR_USERS_TABLE =
            "classpath:db-scripts/users/clear-users-table.sql";

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @Sql(scripts = {
            ADD_DOTA_BOOK,
            ADD_FICTION_BOOK,
            ADD_USER_SHOPPING_CART,
            ADD_DOTA_CART_ITEM,
            ADD_FICTION_CART_ITEM,
            ADD_USER_TO_TABLE
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            CLEAR_CART_ITEMS_TABLE,
            CLEAR_SHOPPING_CARTS_TABLE,
            CLEAR_USERS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test findByUserId() to find specific shopping cart with cart items")
    void findByUserId_existShoppingCart_shouldReturnShoppingCart() {
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(USER.getId());
        assertTrue(actual.isPresent());
        ShoppingCart shoppingCart = actual.get();
        assertEquals(SHOPPING_CART, shoppingCart);
        assertEquals(SHOPPING_CART_ITEM_SET, shoppingCart.getCartItems());
    }

    @Test
    @Sql(scripts = {
            ADD_DOTA_BOOK,
            ADD_FICTION_BOOK,
            ADD_USER_SHOPPING_CART,
            ADD_USER_TO_TABLE
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            CLEAR_CART_ITEMS_TABLE,
            CLEAR_SHOPPING_CARTS_TABLE,
            CLEAR_USERS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test findByUserId() to find specific shopping cart with cart items")
    void findByUserId_existEmptyShoppingCart_shouldReturnShoppingCart() {
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(USER.getId());
        assertTrue(actual.isPresent());
        ShoppingCart shoppingCart = actual.get();
        assertEquals(EMPTY_SHOPPING_CART, shoppingCart);
        assertTrue(shoppingCart.getCartItems().isEmpty());
    }

    @Test
    @Sql(scripts = {
            ADD_USER_TO_TABLE
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            CLEAR_USERS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Test findByUserId() to find non exist shopping cart")
    void findByUserId_nonExistShoppingCart_shouldReturnEmptyOptional() {
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(USER.getId());
        assertTrue(actual.isEmpty());
    }
}
