package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order.StatusUpdateResponseDto;
import com.parkhomovsky.bookstore.dto.orderitem.OrderItemDto;
import com.parkhomovsky.bookstore.enums.OrderStatus;
import com.parkhomovsky.bookstore.exception.EmptyShoppingCartException;
import com.parkhomovsky.bookstore.exception.EntityNotFoundException;
import com.parkhomovsky.bookstore.mapper.OrderItemsMapper;
import com.parkhomovsky.bookstore.mapper.OrderMapper;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderMapper orderMapper;
    private final OrderItemsMapper orderItemsMapper;
    private final UserService userService;

    public OrderDto process(OrderPlaceRequestDto orderPlaceRequestDto) {
        Set<OrderItem> orderItems = getOrderItemsFromShoppingCart();
        User authenticatedUser = userService.getAuthenticatedUser();
        Order order = buildOrder(orderPlaceRequestDto.getShippingAddress(),
                orderItems, authenticatedUser);
        orderRepository.save(order);
        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(orderItemsMapper::toDto)
                .collect(Collectors.toList());
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        OrderDto orderDto = buildOrderDto(order, orderItemDtos);
        orderItemsRepository.saveAll(orderItems);
        return orderDto;
    }

    @Override
    public List<OrderDto> getAll(Pageable pageable) {
        User authenticatedUser = userService.getAuthenticatedUser();
        List<Order> orders = orderRepository.findAllByUserWithItems(authenticatedUser);
        return orderItemsToOrderDtos(orders);
    }

    @Override
    @Transactional
    public StatusUpdateResponseDto updateStatus(Long orderId,
                                                OrderUpdateStatusRequest updateStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id "
                        + orderId + " not found"));
        OrderStatus newOrderStatus = updateStatusRequest.getOrderStatus();
        order.setOrderStatus(newOrderStatus);
        order = orderRepository.save(order);
        return orderMapper.toUpdateResponse(order);
    }

    @Override
    public List<OrderItemDto> getOrderItems(Pageable pageable, Long orderId) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Order order = orderRepository.findByUserAndIdWithItems(orderId, authenticatedUser)
                .orElseThrow(() -> new EntityNotFoundException("Order with id "
                        + orderId + " not found"));
        return orderItemsToOrderItemsDto(order.getOrderItems().stream()
                .toList());
    }

    @Override
    public OrderItemDto getOrderItemByid(Long orderId, Long itemId) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Optional<OrderItem> orderItem =
                orderItemsRepository.findByIdAndOrderIdAndUser(itemId, orderId, authenticatedUser);
        return orderItemsMapper.toDto(orderItem.orElseThrow(() ->
                new EntityNotFoundException("Order item with id "
                        + itemId + " not found in order with id " + orderId)));
    }

    private List<OrderDto> orderItemsToOrderDtos(List<Order> orders) {
        return orders.stream()
                .map(order -> {
                    List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                            .map(orderItemsMapper::toDto)
                            .collect(Collectors.toList());
                    return buildOrderDto(order, orderItemDtos);
                })
                .collect(Collectors.toList());
    }

    private Set<OrderItem> getOrderItemsFromShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart();
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new EmptyShoppingCartException(
                    "Empty shopping cart during processing order. "
                            + "Shopping cart id: " + shoppingCart.getId());
        }
        shoppingCartService.clearShoppingCart();
        return cartItems.stream()
                .map(orderItemsMapper::cartItemToOrderItem)
                .collect(Collectors.toSet());
    }

    private Order buildOrder(String shippingAddress,
                             Set<OrderItem> orderItems,
                             User authenticatedUser) {
        Order order = new Order();
        order.setShippingAddress(shippingAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(authenticatedUser);
        order.setTotal(getTotalPrice(orderItems));
        order.setOrderItems(orderItems);
        return order;
    }

    private BigDecimal getTotalPrice(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderDto buildOrderDto(Order order, List<OrderItemDto> orderItemsDtos) {
        OrderDto orderDto = orderMapper.toDto(order);
        orderDto.setOrderItems(orderItemsDtos);
        return orderDto;
    }

    private List<OrderItemDto> orderItemsToOrderItemsDto(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItemsMapper::toDto)
                .collect(Collectors.toList());
    }
}
