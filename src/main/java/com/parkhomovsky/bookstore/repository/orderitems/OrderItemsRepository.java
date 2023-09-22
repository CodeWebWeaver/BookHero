package com.parkhomovsky.bookstore.repository.orderitems;

import com.parkhomovsky.bookstore.model.OrderItem;
import com.parkhomovsky.bookstore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.order o "
            + "WHERE oi.id = :id AND oi.order.id = :orderId AND o.user = :user")
    Optional<OrderItem> findByIdAndOrderIdAndUser(Long id, Long orderId, User user);

}
