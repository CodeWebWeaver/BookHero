package com.parkhomovsky.bookstore.repository.item;

import com.parkhomovsky.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
