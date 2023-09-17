package com.bookshop.controller;

import com.bookshop.dto.cart.CartDto;
import com.bookshop.dto.cart.CartItemDtoResponse;
import com.bookshop.dto.cart.CreateCartItemDto;
import com.bookshop.dto.cart.PutCartItemDto;
import com.bookshop.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Carts", description = "Operations related to carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CartDto getCartInfo(UserDetails userDetails) {
        return cartService.getCartInfo(userDetails);
    }

    @PostMapping
    public CartItemDtoResponse createCartItem(UserDetails userDetails, @RequestBody @Valid CreateCartItemDto request) {
        return cartService.createCartItem(userDetails, request);
    }

    @PutMapping("/cart-items/{cartItemId}")
    public CartItemDtoResponse updateCartItem(UserDetails userDetails, @PathVariable int cartItemId, @RequestBody @Valid PutCartItemDto quantity) {
        return cartService.updateCartItem(userDetails, cartItemId, quantity);
    }
}
