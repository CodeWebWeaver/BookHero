package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.CartItemMapper;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.repository.item.CartItemRepository;
import com.parkhomovsky.bookstore.service.BookService;
import com.parkhomovsky.bookstore.service.CartItemService;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartService shoppingCartService;
    private final BookService bookService;

    @Override
    public CartItemDto add(Long cartItemId, AddCartItemRequestDto addCartItemRequestDto) {
        Optional<CartItem> possibleCartItem = getCartItemById(cartItemId);
        if (possibleCartItem.isPresent()) {
            CartItem cartItem = addQuantityToCartItem(possibleCartItem.get(),
                    addCartItemRequestDto.getQuantity());
            cartItemRepository.save(cartItem);
            return cartItemMapper.toDto(cartItem);
        }
        throw new EntityNotFoundException("No item found with provided cartItemId: "
                + cartItemId);
    }

    @Override
    public CartItemDto create(CreateCartItemRequestDto cartItemDto) {
        Optional<CartItem> possibleCartItem = getCartItemByBookId(cartItemDto.getBookId());
        if (possibleCartItem.isPresent()) {
            CartItem cartItem = addQuantityToCartItem(possibleCartItem.get(),
                    cartItemDto.getQuantity());
            cartItemRepository.save(cartItem);
            return cartItemMapper.toDto(cartItem);
        }
        CartItem requestCartItem = createNewCartItem(cartItemDto);
        CartItem savedCartItem = cartItemRepository.save(requestCartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public CartItemDto delete(Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart();
        Set<CartItem> cartItems = getCartItemsListForShoppingCart(shoppingCart);
        Optional<CartItem> possibleCartItem = findCartItemById(cartItems, cartItemId);
        if (possibleCartItem.isEmpty()) {
            throw new EntityNotFoundException(
                    "Cart item with provided id not found. id:" + cartItemId);
        }
        CartItem deleteCartItem = possibleCartItem.get();
        cartItemRepository.delete(deleteCartItem);
        return cartItemMapper.toDto(deleteCartItem);
    }

    private Set<CartItem> getCartItemsListForShoppingCart(ShoppingCart shoppingCart) {
        List<CartItem> cartItemsList =
                cartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
        cartItemsList.forEach(cartItem -> cartItem.setShoppingCart(shoppingCart));
        return new HashSet<>(cartItemsList);
    }

    private Optional<CartItem> findCartItemById(Set<CartItem> cartItemsList, Long cartItemId) {
        return cartItemsList.stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                .findFirst();
    }

    private Optional<CartItem> findCartItemByBookId(Set<CartItem> cartItemsList, Long bookId) {
        return cartItemsList.stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(bookId))
                .findFirst();
    }

    private CartItem createNewCartItem(CreateCartItemRequestDto cartItemDto) {
        CartItem requestCartItem = cartItemMapper.toModel(cartItemDto);
        Book bookFromId = bookService.getBookFromId(requestCartItem.getBook().getId());
        requestCartItem.setBook(bookFromId);
        requestCartItem.setShoppingCart(shoppingCartService.getShoppingCart());
        return requestCartItem;
    }

    private CartItem addQuantityToCartItem(CartItem cartItem, int quantity) {
        cartItem.setQuantity(cartItem
                .getQuantity() + quantity);
        return cartItem;
    }

    private Optional<CartItem> getCartItemById(Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart();
        Set<CartItem> cartItems = getCartItemsListForShoppingCart(shoppingCart);
        return findCartItemById(cartItems, cartItemId);
    }

    private Optional<CartItem> getCartItemByBookId(Long bookId) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart();
        Set<CartItem> cartItems = getCartItemsListForShoppingCart(shoppingCart);
        return findCartItemByBookId(cartItems, bookId);
    }
}
