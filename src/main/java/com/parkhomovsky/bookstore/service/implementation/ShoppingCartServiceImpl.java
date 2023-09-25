package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.CartItemMapper;
import com.parkhomovsky.bookstore.mapper.ShoppingCartMapper;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.cart.ShoppingCartRepository;
import com.parkhomovsky.bookstore.repository.item.CartItemRepository;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import com.parkhomovsky.bookstore.service.UserService;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getUserShoppingCartDto() throws EntityNotFoundException {
        User user = (User) userService.getAuthenticatedUserDetails();
        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartOptional
                .map(this::buildExistShoppingCartDto)
                .orElseGet(() -> createNewShoppingCartDto(user));
    }

    @Override
    @Transactional
    public void clearShoppingCart() {
        cartItemRepository.deleteAllByShoppingCartId(getShoppingCart().getId());
    }

    @Override
    public ShoppingCart getShoppingCart() {
        User user = (User) userService.getAuthenticatedUserDetails();
        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartOptional.orElseGet(() -> createNewShoppingCart(user));
    }

    private ShoppingCartDto buildExistShoppingCartDto(ShoppingCart shoppingCart) {
        Set<CartItemDto> cartItemDtoSet = shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.setCartItemDtos(cartItemDtoSet);
        return shoppingCartDto;
    }

    private ShoppingCartDto createNewShoppingCartDto(User user) {
        return shoppingCartMapper.toDto(createNewShoppingCart(user));
    }

    private ShoppingCart createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }
}
