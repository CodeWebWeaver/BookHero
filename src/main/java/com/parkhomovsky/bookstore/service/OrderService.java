package com.parkhomovsky.bookstore.service;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order.StatusUpdateResponseDto;
import com.parkhomovsky.bookstore.dto.orderitem.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderDto process(OrderPlaceRequestDto orderPlaceRequestDto,
                     Authentication authentication);

    StatusUpdateResponseDto updateStatus(Long orderId,
                                         OrderUpdateStatusRequest updateStatusRequest);

    List<OrderItemDto> getOrderItemsDto(Pageable pageable, Long orderId,
                                        Authentication authentication);

    OrderItemDto getOrderItemByidDto(Long orderId, Long itemId,
                                     Authentication authentication);

    List<OrderDto> getAll(Pageable pageable, Authentication authentication);
}
