package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order_item.OrderItemDto;
import com.parkhomovsky.bookstore.enums.Status;
import com.parkhomovsky.bookstore.exception.UserNotAuthenticatedException;
import com.parkhomovsky.bookstore.mapper.OrderItemsMapper;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.Order;
import com.parkhomovsky.bookstore.model.OrderItem;
import com.parkhomovsky.bookstore.model.ShoppingCart;
import com.parkhomovsky.bookstore.model.User;
import com.parkhomovsky.bookstore.repository.order.OrderRepository;
import com.parkhomovsky.bookstore.repository.orderitems.OrderItemsRepository;
import com.parkhomovsky.bookstore.service.OrderService;
import com.parkhomovsky.bookstore.service.ShoppingCartService;
import com.parkhomovsky.bookstore.service.UserService;
import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderItemsMapper orderItemsMapper;
    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UserService userService;

    @Override
    public Set<OrderItemDto> process(OrderPlaceRequestDto orderPlaceRequestDto) throws UserNotAuthenticatedException {
        Order order = new Order();
        order.setShippingAddress(orderPlaceRequestDto.getShippingAddress());

        ShoppingCart shoppingCart = shoppingCartService.getUserShoppingCart();
        Set<CartItem> cartItems = shoppingCartService.getCartItemsSetForShoppingCart(shoppingCart);
        Set<OrderItem> orderItems = cartItems.stream()
                .map(orderItemsMapper::toModel)
                .collect(Collectors.toSet());

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        order.setOrderItems(orderItems);
        order.setUser((User) userService.getUser());

        BigDecimal sum = orderItems.stream()
                .map(orderItem -> orderItem.getBook().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(new BigDecimal(0.0), BigDecimal::add);
        order.setTotal(sum);
        orderRepository.save(order);

        // Устанавливаем order в каждом OrderItem
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        // Сохраняем OrderItem
        List<OrderItem> orderItemsList = orderItemsRepository.saveAll(orderItems);

        return orderItemsList.stream()
                .map(orderItemsMapper::toDto)
                .collect(Collectors.toSet());
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
