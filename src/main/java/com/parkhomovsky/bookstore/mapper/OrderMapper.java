package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.StatusUpdateResponseDto;
import com.parkhomovsky.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", ignore = true)
    OrderDto toDto(Order order);

    @Mapping(target = "userId", source = "user.id")
    StatusUpdateResponseDto toUpdateResponse(Order order);

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    Order toModel(OrderDto order);
}
