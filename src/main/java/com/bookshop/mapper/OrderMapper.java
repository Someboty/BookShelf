package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.order.response.OrderDto;
import com.bookshop.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);
}
