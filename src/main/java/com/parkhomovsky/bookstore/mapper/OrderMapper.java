package com.parkhomovsky.bookstore.mapper;

import com.parkhomovsky.bookstore.config.MapperConfiguration;
import com.parkhomovsky.bookstore.dto.order.OrderDto;
import com.parkhomovsky.bookstore.dto.order.UpdateResponseDto;
import com.parkhomovsky.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", ignore = true)
    OrderDto toDto(Order order);

    @Mapping(target = "userId", source = "user.id")
    UpdateResponseDto toUpdateResponse(Order order);

    Order toModel(OrderDto order);
}
