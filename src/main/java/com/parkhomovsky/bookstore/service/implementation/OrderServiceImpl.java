package com.parkhomovsky.bookstore.service.implementation;

import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.OrderPlaceRequestDto;
import com.parkhomovsky.bookstore.dto.order.OrderUpdateStatusRequest;
import com.parkhomovsky.bookstore.dto.order.UpdateResponseDto;
import com.parkhomovsky.bookstore.dto.orderitem.OrderItemDto;
import com.parkhomovsky.bookstore.enums.Status;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final OrderItemsMapper orderItemsMapper;

    @Override
    public OrderDto process(OrderPlaceRequestDto orderPlaceRequestDto)
            throws EmptyShoppingCartException {
        Set<OrderItem> orderItems = getOrderItemsDtoFromShoppingCart();
        Order order = buildOrder(orderPlaceRequestDto.getShippingAddress(), orderItems);
        orderRepository.save(order);
        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(orderItemsMapper::toDto)
                .collect(Collectors.toList());
        order.setOrderItems(null);
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        OrderDto orderDto = orderMapper.toDto(order);
        orderDto.setOrderItems(orderItemDtos);
        orderItemsRepository.saveAll(orderItems);
        return orderDto;
    }

    @Override
    public List<OrderDto> getAll(Pageable pageable, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<Order> orders = orderRepository.findAllByUser(currentUser);
        List<OrderItem> orderItems = getUserOrderItems(currentUser, orders);
        return orderItemsToOrderDtos(orders, orderItems);
    }

    @Override
    public UpdateResponseDto updateStatus(Long orderId,
                                          OrderUpdateStatusRequest updateStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id "
                        + orderId + " not found"));
        Status newStatus = updateStatusRequest.getStatus();
        order.setStatus(newStatus);
        order = orderRepository.save(order);
        return orderMapper.toUpdateResponse(order);
    }

    @Override
    public List<OrderItemDto> getOrderItemsDto(Pageable pageable, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id "
                        + orderId + " not found"));
        Long currentUserId = userService.getUserId();
        List<OrderItem> orderItems = orderItemsRepository.findByOrder(order, currentUserId);
        return orderItemsToOrderItemsDto(orderItems);
    }

    @Override
    public OrderItemDto getOrderItemByidDto(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id "
                        + orderId + " not found"));
        Long currentUserId = userService.getUserId();
        Optional<OrderItem> orderItem =
                orderItemsRepository.findByIdAndOrder(itemId, order, currentUserId);
        return orderItemsMapper.toDto(orderItem.orElseThrow(() ->
                new EntityNotFoundException("Order item with id "
                        + itemId + " not found in order with id " + orderId)));
    }

    private List<OrderItem> getUserOrderItems(User currentUser, List<Order> orders) {
        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
        return orderItemsRepository
                .findAllByOrderIdInAndUserId(orderIds, currentUser.getId());
    }

    private List<OrderDto> orderItemsToOrderDtos(List<Order> orders, List<OrderItem> orderItems) {
        Map<Long, List<OrderItem>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getOrder().getId()));
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> orderItemsForOrder =
                    orderItemMap.getOrDefault(order.getId(), Collections.emptyList());
            List<OrderItemDto> orderItemDtos = orderItemsToOrderItemsDto(orderItemsForOrder);
            OrderDto orderDto = buildOrderDto(order, orderItemDtos);
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    private Set<OrderItem> getOrderItemsDtoFromShoppingCart()
            throws EmptyShoppingCartException {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart();
        Set<CartItem> cartItems = shoppingCartService.getCartItemsSetForShoppingCart(shoppingCart);
        if (cartItems.isEmpty()) {
            throw new EmptyShoppingCartException(
                    "Empty shopping cart during processing order. "
                            + "Shopping cart id: " + shoppingCart.getId());
        }
        shoppingCartService.clearShoppingCart();
        return cartItems.stream()
                .map(orderItemsMapper::toModel)
                .collect(Collectors.toSet());
    }

    private Order buildOrder(String shippingAddress, Set<OrderItem> orderItems) {
        Order order = new Order();
        order.setShippingAddress(shippingAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        order.setUser((User) userService.getUser());
        order.setTotal(getTotalPrice(orderItems));
        order.setOrderItems(orderItems);
        return order;
    }

    private BigDecimal getTotalPrice(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(new BigDecimal("0.0"), BigDecimal::add);
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
