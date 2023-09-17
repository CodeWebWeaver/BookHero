package com.parkhomovsky.bookstore.repository.orderitems;

import com.parkhomovsky.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
}
