package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order_item.OrderItemDto;
import com.parkhomovsky.bookstore.model.OrderItem;
import java.awt.print.Pageable;
import java.util.List;

public interface OrderService {
    List<OrderItemDto> process(OrderPlaceRequestDto orderPlaceRequestDto);
    List<OrderDto> getAll(Pageable pageable);
    OrderDto updateStatus(OrderUpdateStatusRequest updateStatusRequest);
    List<OrderItemDto> getOrderItems(Long orderId);
    OrderItem getOrderItem(Long orderId, Long itemId);
}
