package com.parkhomovsky.bookstore.repository.cart;

import com.parkhomovsky.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("FROM ShoppingCart s LEFT JOIN FETCH s.cartItems WHERE s.user.email = :username")
    Optional<ShoppingCart> findByUsername(String username);
}
