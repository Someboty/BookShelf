package com.bookshelf.service;

import com.bookshelf.dto.cart.request.CreateCartItemDto;
import com.bookshelf.dto.cart.request.PutCartItemDto;
import com.bookshelf.dto.cart.response.CartDto;
import com.bookshelf.dto.cart.response.CartItemDtoResponse;

public interface CartService {
    CartDto getCartInfo(Long userId);

    CartItemDtoResponse createCartItem(Long userId, CreateCartItemDto request);

    CartItemDtoResponse updateCartItem(Long userId, Long cartItemId, PutCartItemDto request);

    void removeCartItem(Long userId, Long cartItemId);

    void clearCart(Long userId);
}
