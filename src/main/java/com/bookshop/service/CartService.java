package com.bookshop.service;

import com.bookshop.dto.cart.CartDto;
import com.bookshop.dto.cart.CartItemDtoResponse;
import com.bookshop.dto.cart.CreateCartItemDto;
import com.bookshop.dto.cart.PutCartItemDto;

public interface CartService {
    CartDto getCartInfo(Long userId);

    CartItemDtoResponse createCartItem(Long userId, CreateCartItemDto request);

    CartItemDtoResponse updateCartItem(Long userId, Long cartItemId, PutCartItemDto request);

    void removeCartItem(Long userId, Long cartItemId);

    void clearCart(Long userId);
}
