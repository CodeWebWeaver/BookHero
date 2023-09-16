package com.parkhomovsky.bookstore.repository.item;

import com.parkhomovsky.bookstore.model.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT c FROM CartItem c JOIN c.shoppingCart sc WHERE sc.id = :shoppingCartId")
    List<CartItem> findByShoppingCartId(Long shoppingCartId);
}
