package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.dto.item.CartItemDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.CartItemMapper;
import com.parkhomovsky.bookstore.mapper.ShoppingCartMapper;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.cart.ShoppingCartRepository;
import com.parkhomovsky.bookstore.repository.item.CartItemRepository;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import com.parkhomovsky.bookstore.service.UserService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        User user = (User) userService.getAuthenticatedUser();
        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartOptional
                .map(this::buildExistShoppingCartDto)
                .orElseGet(() -> createNewShoppingCartDto(user));
    }

    private ShoppingCartDto buildExistShoppingCartDto(ShoppingCart shoppingCart) {
        List<CartItem> cartItemsListForShoppingCart = getCartItemsListForShoppingCart(shoppingCart);
        Set<CartItemDto> cartItemDtoSet = cartItemsListForShoppingCart.stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.setCartItemDtos(cartItemDtoSet);
        return shoppingCartDto;
    }

    @Override
    public Set<CartItem> getCartItemsSetForShoppingCart(ShoppingCart shoppingCart) {
        List<CartItem> cartItemsList =
                cartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
        cartItemsList.forEach(cartItem -> cartItem.setShoppingCart(shoppingCart));
        return new HashSet<>(cartItemsList);
    }

    @Override
    @Transactional
    public void clearShoppingCart() {
        cartItemRepository.deleteAllByShoppingCartId(getShoppingCart().getId());
    }

    private List<CartItem> getCartItemsListForShoppingCart(ShoppingCart shoppingCart) {
        return cartItemRepository.findAllByShoppingCartId(shoppingCart.getId())
                .stream()
                .peek(cartItem -> cartItem.setShoppingCart(shoppingCart))
                .collect(Collectors.toList());
    }

    @Override
    public ShoppingCart getShoppingCart() {
        ShoppingCartDto userShoppingCartDto = getUserShoppingCartDto();
        return shoppingCartMapper
                .toModel(userShoppingCartDto);
    }

    private ShoppingCartDto createNewShoppingCartDto(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }
}
