package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.orderitem.OrderItemDto;
import com.parkhomovsky.bookstore.model.CartItem;
import com.parkhomovsky.bookstore.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface OrderItemsMapper {
    OrderItem toModel(CartItem cartItem);

    OrderItemDto toDto(OrderItem orderItem);
}
