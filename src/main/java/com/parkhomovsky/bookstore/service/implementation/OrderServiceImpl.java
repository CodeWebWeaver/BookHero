package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order_item.OrderItemDto;
import com.parkhomovsky.bookstore.model.OrderItem;
import com.parkhomovsky.bookstore.repository.order.OrderRepository;
import com.parkhomovsky.bookstore.service.OrderService;
import java.awt.print.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public List<OrderItemDto> process(OrderPlaceRequestDto orderPlaceRequestDto) {
        return null;
    }

    @Override
    public List<OrderDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public OrderDto updateStatus(OrderUpdateStatusRequest updateStatusRequest) {
        return null;
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        return null;
    }

    @Override
    public OrderItem getOrderItem(Long orderId, Long itemId) {
        return null;
    }
}
