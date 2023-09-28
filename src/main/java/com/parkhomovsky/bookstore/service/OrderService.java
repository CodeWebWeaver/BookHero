package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order.StatusUpdateResponseDto;
import com.parkhomovsky.bookstore.dto.orderitem.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto process(OrderPlaceRequestDto orderPlaceRequestDto);

    StatusUpdateResponseDto updateStatus(Long orderId,
                                         OrderUpdateStatusRequest updateStatusRequest);

    List<OrderItemDto> getOrderItems(Pageable pageable, Long orderId);

    OrderItemDto getOrderItemByid(Long orderId, Long itemId);

    List<OrderDto> getAll(Pageable pageable);
}
