package com.parkhomovsky.bookstore.service.implementation;

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
import com.parkhomovsky.bookstore.repository.book.BookRepository;
import com.parkhomovsky.bookstore.repository.item.CartItemRepository;
import com.parkhomovsky.bookstore.service.CartItemService;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
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

  @Override
  public CartItemDto addCartItem(Long id, AddCartItemRequestDto cartItemDto) throws UserNotAuthenticatedException {
    Book book = getBookFromId(id);
    ShoppingCart shoppingCart = shoppingCartMapper.toModel(shoppingCartService.getShoppingCart());
    Set<CartItem> cartItems = shoppingCart.getCartItems();
    for (CartItem cartItem : cartItems) {
      if (cartItem.getBook().getId()
              .equals(book.getId())) {
        cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(savedCartItem);
      }
    }
    throw new EntityNotFoundException("No cart item found for adding. Create new request");
  }

  @Override
  public CartItemDto createCartItem(CreateCartItemRequestDto cartItemDto) throws UserNotAuthenticatedException {
    ShoppingCart shoppingCart = shoppingCartMapper.toModel(shoppingCartService.getShoppingCart());
    CartItem cartItem = cartItemMapper.toModel(cartItemDto);
    Book bookFromId = getBookFromId(cartItem.getBook().getId());
    cartItem.setBook(bookFromId);
    cartItem.setShoppingCart(shoppingCart);
    CartItem savedCartItem = cartItemRepository.save(cartItem);
    return cartItemMapper.toDto(savedCartItem);
  }

  private Book getBookFromId(Long id) {
    Optional<Book> bookById = bookRepository.findById(id);
    if (bookById.isEmpty()) {
      throw new EntityNotFoundException("Can`t find any book with id: "
              + id + " during add cart item to cart");
    }
    return bookById.get();
  }
}
