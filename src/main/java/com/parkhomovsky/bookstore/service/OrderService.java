package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order.UpdateResponseDto;
import com.parkhomovsky.bookstore.dto.orderitem.OrderItemDto;
import com.parkhomovsky.bookstore.exception.EmptyShoppingCartException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderDto process(OrderPlaceRequestDto orderPlaceRequestDto)
            throws EmptyShoppingCartException;

    UpdateResponseDto updateStatus(Long orderId, OrderUpdateStatusRequest updateStatusRequest);

    List<OrderItemDto> getOrderItemsDto(Pageable pageable, Long orderId);

    OrderItemDto getOrderItemByidDto(Long orderId, Long itemId);

    List<OrderDto> getAll(Pageable pageable, Authentication authentication);
}
