package com.bookshop.service;

import com.bookshop.dto.order.OrderDto;
import com.bookshop.dto.order.OrderItemDto;
import com.bookshop.dto.order.ShippingAddressRequestDto;
import com.bookshop.dto.order.StatusRequestDto;
import java.util.Set;

public interface OrderService {
    Set<OrderDto> getHistory(Long userId);

    OrderDto createOrder(Long userId, ShippingAddressRequestDto request);

    OrderDto updateStatus(Long orderId, StatusRequestDto request);

    Set<OrderItemDto> getAllItems(Long orderId);

    OrderItemDto getItemById(Long orderId, Long itemId);
}
