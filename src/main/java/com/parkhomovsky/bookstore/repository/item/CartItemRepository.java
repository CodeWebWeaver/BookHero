package com.parkhomovsky.bookstore.repository.item;

import com.parkhomovsky.bookstore.model.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT c FROM CartItem c JOIN c.shoppingCart sc WHERE sc.id = :shoppingCartId")
    List<CartItem> findByShoppingCartId(Long shoppingCartId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.shoppingCart.id = :cartId")
    void deleteAllByShoppingCartId(@Param("cartId") Long cartId);
}
