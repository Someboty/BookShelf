package com.bookshop.service.impl;

import com.bookshop.dto.order.request.ShippingAddressRequestDto;
import com.bookshop.dto.order.request.StatusRequestDto;
import com.bookshop.dto.order.response.OrderDto;
import com.bookshop.dto.order.response.OrderItemDto;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.OrderItemMapper;
import com.bookshop.mapper.OrderMapper;
import com.bookshop.model.CartItem;
import com.bookshop.model.Order;
import com.bookshop.model.OrderItem;
import com.bookshop.model.ShoppingCart;
import com.bookshop.repository.cart.CartRepository;
import com.bookshop.repository.order.OrderItemRepository;
import com.bookshop.repository.order.OrderRepository;
import com.bookshop.service.CartService;
import com.bookshop.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Set<OrderDto> getHistory(Long userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, ShippingAddressRequestDto request) {
        ShoppingCart cart = cartRepository.getShoppingCartByUser_Id(userId);
        Order order = formOrder(cart, request.getShippingAddress());
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getOrderItems());
        cartService.clearCart(userId);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto updateStatus(Long orderId, StatusRequestDto request) {
        Order order = findOrderById(orderId);
        order.setStatus(Order.Status.valueOf(request.getStatus().toUpperCase()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public Set<OrderItemDto> getAllItems(Long orderId) {
        findOrderById(orderId);
        return orderItemRepository.findAllByOrderId(orderId).stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public OrderItemDto getItemById(Long orderId, Long itemId) {
        return orderItemMapper.toDto(findOrderById(orderId).getOrderItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item by id " + itemId)
                ));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id " + orderId));
    }

    private Order formOrder(ShoppingCart cart, String address) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(address);
        order.setOrderItems(convertToOrderItems(cart.getCartItems(), order));
        order.setTotal(calculatingTotals(cart.getCartItems()));
        return order;
    }

    private Set<OrderItem> convertToOrderItems(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> createOrderItem(cartItem, order))
                .collect(Collectors.toSet());
    }

    private OrderItem createOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(cartItem.getId());
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice().multiply(
                BigDecimal.valueOf(cartItem.getQuantity())));
        orderItem.setOrder(order);
        return orderItem;
    }

    private BigDecimal calculatingTotals(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(i -> i.getBook()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
