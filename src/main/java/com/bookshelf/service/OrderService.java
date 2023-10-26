package com.bookshelf.service;

import com.bookshelf.dto.order.request.ShippingAddressRequestDto;
import com.bookshelf.dto.order.request.StatusRequestDto;
import com.bookshelf.dto.order.response.OrderDto;
import com.bookshelf.dto.order.response.OrderItemDto;
import java.util.Set;

public interface OrderService {
    Set<OrderDto> getHistory(Long userId);

    OrderDto createOrder(Long userId, ShippingAddressRequestDto request);

    OrderDto updateStatus(Long orderId, StatusRequestDto request);

    Set<OrderItemDto> getAllItems(Long orderId);

    OrderItemDto getItemById(Long orderId, Long itemId);
}
