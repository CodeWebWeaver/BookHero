package com.parkhomovsky.bookstore.repository.orderitems;

import com.parkhomovsky.bookstore.model.Order;
import com.parkhomovsky.bookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "WHERE oi.id = :itemId AND oi.order = :order AND o.user.id = :userId")
    Optional<OrderItem> findByIdAndOrder(@Param("itemId") Long itemId,
                                         @Param("order") Order order,
                                         @Param("userId") Long userId);

    @Query("SELECT oi FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "WHERE oi.order = :order AND o.user.id = :userId ")
    List<OrderItem> findByOrder(@Param("order") Order order,
                                @Param("userId") Long userId);

    @Query("SELECT oi FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "WHERE o.id IN :orderIds AND o.user.id = :userId")
    List<OrderItem> findAllByOrderIdInAndUserId(@Param("orderIds") List<Long> orderIds,
                                                @Param("userId") Long userId);

}
