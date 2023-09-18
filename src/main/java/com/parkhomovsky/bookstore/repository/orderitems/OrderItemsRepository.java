package com.parkhomovsky.bookstore.repository.orderitems;

import com.parkhomovsky.bookstore.model.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.order o WHERE o.id = :orderId")
    List<OrderItem> findAllByOrderId(Long orderId);

    @Query("SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.order")
    List<OrderItem> findAllWithOrder();

    @Query("SELECT oi FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "WHERE o.id IN :orderIds AND o.user.id = :userId")
    List<OrderItem> findAllByOrderIdInAndUserId(List<Long> orderIds, Long userId);

}
