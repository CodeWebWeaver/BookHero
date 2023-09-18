package com.parkhomovsky.bookstore.repository.order;

import com.parkhomovsky.bookstore.model.Order;
import com.parkhomovsky.bookstore.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user = :user")
    List<Order> findAllByUser(User user);

}
