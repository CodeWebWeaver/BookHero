package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.cart.ShoppingCartDto;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;
import com.parkhomovsky.bookstore.mapper.ShoppingCartMapper;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.cart.ShoppingCartRepository;
import com.parkhomovsky.bookstore.repository.user.UserRepository;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import java.util.HashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getUserShoppingCart()
            throws UserNotAuthenticatedException, EntityNotFoundException {
        String username = getUsernameFromAuthentication();
        ShoppingCart shoppingCart = findOrCreateShoppingCartForUser(username);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private String getUsernameFromAuthentication() throws UserNotAuthenticatedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UserNotAuthenticatedException("User not authenticated");
        }
        return authentication.getName();
    }

    private ShoppingCart findOrCreateShoppingCartForUser(String username) {
        Optional<ShoppingCart> shoppingCartOptional =
                shoppingCartRepository.findByUsername(username);
        if (shoppingCartOptional.isPresent()) {
            return shoppingCartOptional.get();
        }
        Optional<User> userOptional = userRepository.findByEmail(username);
        User user = userOptional.orElseThrow(() ->
                new EntityNotFoundException("User not found for email: " + username));
        return createShoppingCartForUser(user);
    }

    private ShoppingCart createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }
}
