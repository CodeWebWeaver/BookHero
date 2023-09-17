package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.dto.item.AddCartItemRequestDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.dto.item.CreateCartItemRequestDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.exception.InvalidRequestParametersException;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;
import com.parkhomovsky.bookstore.mapper.CartItemMapper;
import com.parkhomovsky.bookstore.mapper.ShoppingCartMapper;
import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.repository.book.BookRepository;
import com.parkhomovsky.bookstore.repository.item.CartItemRepository;
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
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    public CartItemDto add(Long cartItemId, AddCartItemRequestDto addCartItemRequestDto)
            throws UserNotAuthenticatedException {
        ShoppingCart shoppingCart = shoppingCartService.getUserShoppingCart();
        Set<CartItem> cartItems = shoppingCartService.getCartItemsSetForShoppingCart(shoppingCart);
        Optional<CartItem> possibleCartItem = findCartItemById(cartItems, cartItemId);
        if (possibleCartItem.isPresent()) {
            CartItem presentCartItem = possibleCartItem.get();
            presentCartItem.setQuantity(presentCartItem
                    .getQuantity() + addCartItemRequestDto.getQuantity());
            cartItemRepository.save(presentCartItem);
            return cartItemMapper.toDto(presentCartItem);
        }
        throw new EntityNotFoundException("No item found with provided id");
    }

    @Override
    public CartItemDto create(CreateCartItemRequestDto cartItemDto)
            throws UserNotAuthenticatedException, InvalidRequestParametersException {
        ShoppingCart shoppingCart = shoppingCartService.getUserShoppingCart();
        Set<CartItem> cartItems = shoppingCartService.getCartItemsSetForShoppingCart(shoppingCart);
        Optional<CartItem> possibleCartItem =
                findCartItemByBookId(cartItems, cartItemDto.getBookId());
        CartItem requestCartItem = cartItemMapper.toModel(cartItemDto);
        if (possibleCartItem.isPresent()) {
            CartItem presentCartItem = possibleCartItem.get();
            presentCartItem.setQuantity(presentCartItem
                    .getQuantity() + requestCartItem.getQuantity());
            cartItemRepository.save(presentCartItem);
            return cartItemMapper.toDto(presentCartItem);
        }
        try {
            Book bookFromId = getBookFromId(cartItemDto.getBookId());
            requestCartItem.setBook(bookFromId);
            requestCartItem.setShoppingCart(shoppingCart);
            CartItem savedCartItem = cartItemRepository.save(requestCartItem);
            return cartItemMapper.toDto(savedCartItem);
        } catch (EntityNotFoundException e) {
            throw new InvalidRequestParametersException("Wrong book id: "
                    + cartItemDto.getBookId());
        }

    }

    @Override
    public CartItemDto delete(Long cartItemId)
            throws UserNotAuthenticatedException, InvalidRequestParametersException {
        ShoppingCart shoppingCart = shoppingCartService.getUserShoppingCart();
        Set<CartItem> cartItems = shoppingCartService.getCartItemsSetForShoppingCart(shoppingCart);
        Optional<CartItem> possibleCartItem = findCartItemById(cartItems, cartItemId);
        if (possibleCartItem.isEmpty()) {
            throw new InvalidRequestParametersException(
                    "Cart item with provided id not found. id:" + cartItemId);
        }
        CartItem deleteCartItem = possibleCartItem.get();
        cartItemRepository.delete(deleteCartItem);
        return cartItemMapper.toDto(deleteCartItem);
    }

    private Book getBookFromId(Long id) {
        Optional<Book> bookById = bookRepository.findByIdWithCategory(id);
        if (bookById.isEmpty()) {
            throw new EntityNotFoundException("Can`t find any book with id: "
                    + id + " during add cart item to cart");
        }
        return bookById.get();
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
}
