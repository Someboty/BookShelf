package com.bookshelf.mapper;

import com.bookshelf.config.MapperConfig;
import com.bookshelf.dto.order.response.OrderItemDto;
import com.bookshelf.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}
