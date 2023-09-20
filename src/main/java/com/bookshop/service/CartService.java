package com.bookshop.service;

import com.bookshop.dto.cart.response.CartDto;
import com.bookshop.dto.cart.response.CartItemDtoResponse;
import com.bookshop.dto.cart.request.CreateCartItemDto;
import com.bookshop.dto.cart.request.PutCartItemDto;

public interface CartService {
    CartDto getCartInfo(Long userId);

    CartItemDtoResponse createCartItem(Long userId, CreateCartItemDto request);

    CartItemDtoResponse updateCartItem(Long userId, Long cartItemId, PutCartItemDto request);

    void removeCartItem(Long userId, Long cartItemId);

    void clearCart(Long userId);
}
