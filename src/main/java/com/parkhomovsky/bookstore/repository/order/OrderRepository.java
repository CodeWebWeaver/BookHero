package com.parkhomovsky.bookstore.repository.order;

import com.parkhomovsky.bookstore.model.Order;
import com.parkhomovsky.bookstore.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.user = :user")
    List<Order> findAllByUserWithItems(User user);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findByIdWithItems(Long orderId);
}
