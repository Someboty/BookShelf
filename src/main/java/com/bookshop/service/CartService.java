package com.bookshop.service;

import com.bookshop.dto.cart.CartDto;
import com.bookshop.dto.cart.CartItemDtoResponse;
import com.bookshop.dto.cart.CreateCartItemDto;
import com.bookshop.dto.cart.PutCartItemDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface CartService {
    CartDto getCartInfo(UserDetails userDetails);

    CartItemDtoResponse createCartItem(UserDetails userDetails, CreateCartItemDto request);

    CartItemDtoResponse updateCartItem(UserDetails userDetails, int cartItemId, PutCartItemDto quantity);
}
