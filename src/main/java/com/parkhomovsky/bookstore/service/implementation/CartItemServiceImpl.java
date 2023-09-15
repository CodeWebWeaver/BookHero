package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;
import com.parkhomovsky.bookstore.mapper.CartItemMapper;
import com.parkhomovsky.bookstore.mapper.ShoppingCartMapper;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.book.BookRepository;
import com.parkhomovsky.bookstore.repository.item.CartItemRepository;
import com.parkhomovsky.bookstore.repository.user.UserRepository;
import com.parkhomovsky.bookstore.service.CartItemService;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import com.parkhomovsky.bookstore.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;

    @Override
    public CartItemDto add(Long id, AddCartItemRequestDto cartItemDto)
            throws UserNotAuthenticatedException {
        ShoppingCart shoppingCart = getShoppingCartWithOwner();
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Optional<CartItem> cartItemForAdd = cartItems.stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findFirst();
        CartItem addCartItem = cartItemForAdd.orElseThrow(() ->
                new EntityNotFoundException("No cart item found for adding. Create new request"));
        addCartItem.setQuantity(cartItemDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(addCartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public CartItemDto create(CreateCartItemRequestDto cartItemDto)
            throws UserNotAuthenticatedException {
        ShoppingCart shoppingCart = getShoppingCartWithOwner();
        List<CartItem> cartItemsList = cartItemRepository.findByShoppingCartId(shoppingCart.getId());
        cartItemsList.forEach(cartItem -> cartItem.setShoppingCart(shoppingCart));
        Optional<CartItem> possibleCartItem = cartItemsList.stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(cartItemDto.getBookId()))
                .findFirst();
        CartItem requestCartItem = cartItemMapper.toModel(cartItemDto);
        if (possibleCartItem.isPresent()) {
            CartItem presentCartItem = possibleCartItem.get();
            presentCartItem.setQuantity(presentCartItem.getQuantity() + requestCartItem.getQuantity());
            cartItemRepository.save(presentCartItem);
            return cartItemMapper.toDto(presentCartItem);
        } else {
            Book bookFromId = getBookFromId(cartItemDto.getBookId());
            requestCartItem.setBook(bookFromId);
            requestCartItem.setShoppingCart(shoppingCart);
            CartItem savedCartItem = cartItemRepository.save(requestCartItem);
            return cartItemMapper.toDto(savedCartItem);
        }
    }

    @Override
    public CartItemDto delete(Long cartItemId) throws UserNotAuthenticatedException {
        ShoppingCart shoppingCart =
                shoppingCartMapper.toModel(shoppingCartService.getUserShoppingCart());
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        Optional<CartItem> cartItemForDelete = cartItems.stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                .findFirst();
        if (cartItemForDelete.isEmpty()) {
            throw new EntityNotFoundException(
                    "Cart item with provided id not found. id:" + cartItemId);
        }
        CartItem deleteCartItem = cartItemForDelete.get();
        cartItems.remove(deleteCartItem);
        cartItemRepository.delete(deleteCartItem);
        return cartItemMapper.toDto(deleteCartItem);
    }

    private ShoppingCart getShoppingCartWithOwner() throws UserNotAuthenticatedException {
        ShoppingCartDto userShoppingCartDto = shoppingCartService.getUserShoppingCart();
        ShoppingCart shoppingCart = shoppingCartMapper
                .toModel(userShoppingCartDto);
        Optional<User> shoppingCartOwner = userRepository.findById(userShoppingCartDto.getUserId());
        shoppingCart.setUser(shoppingCartOwner
                .orElseThrow(() -> new EntityNotFoundException("User not found for shopping cart: "
                        + shoppingCart.getId())));
        return shoppingCart;
    }

    private Book getBookFromId(Long id) {
        Optional<Book> bookById = bookRepository.findByIdWithCategory(id);
        if (bookById.isEmpty()) {
            throw new EntityNotFoundException("Can`t find any book with id: "
                    + id + " during add cart item to cart");
        }
        return bookById.get();
    }
}
