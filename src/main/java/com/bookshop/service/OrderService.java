package com.bookshop.service;

import com.bookshop.dto.order.request.ShippingAddressRequestDto;
import com.bookshop.dto.order.request.StatusRequestDto;
import com.bookshop.dto.order.response.OrderDto;
import com.bookshop.dto.order.response.OrderItemDto;
import java.util.Set;

public interface OrderService {
    Set<OrderDto> getHistory(Long userId);

    OrderDto createOrder(Long userId, ShippingAddressRequestDto request);

    OrderDto updateStatus(Long orderId, StatusRequestDto request);

    Set<OrderItemDto> getAllItems(Long orderId);

    OrderItemDto getItemById(Long orderId, Long itemId);
}
