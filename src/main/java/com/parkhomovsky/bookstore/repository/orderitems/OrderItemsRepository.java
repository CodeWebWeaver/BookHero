package com.parkhomovsky.bookstore.repository.orderitems;

import com.parkhomovsky.bookstore.model.Order;
import com.parkhomovsky.bookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.order")
    List<OrderItem> findAllWithOrder();

    @Query("SELECT oi FROM OrderItem oi WHERE oi.id = :itemId AND oi.order = :order")
    Optional<OrderItem> findByIdAndOrder(@Param("itemId") Long itemId, @Param("order") Order order);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order = :order")
    List<OrderItem> findByOrder(@Param("order") Order order);

    @Query("SELECT oi FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "WHERE o.id IN :orderIds AND o.user.id = :userId")
    List<OrderItem> findAllByOrderIdInAndUserId(List<Long> orderIds, Long userId);

}
