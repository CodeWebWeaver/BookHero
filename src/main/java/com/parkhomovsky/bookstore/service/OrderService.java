package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order_item.OrderItemDto;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;
import com.parkhomovsky.bookstore.model.OrderItem;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<OrderItemDto> process(OrderPlaceRequestDto orderPlaceRequestDto) throws UserNotAuthenticatedException;
    List<OrderDto> getAll(Pageable pageable);
    OrderDto updateStatus(OrderUpdateStatusRequest updateStatusRequest);
    List<OrderItemDto> getOrderItems(Long orderId);
    OrderItem getOrderItem(Long orderId, Long itemId);
}
