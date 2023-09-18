package com.bookshop.controller;

import com.bookshop.dto.cart.CartDto;
import com.bookshop.dto.cart.CartItemDtoResponse;
import com.bookshop.dto.cart.CreateCartItemDto;
import com.bookshop.dto.cart.PutCartItemDto;
import com.bookshop.model.User;
import com.bookshop.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public CartDto getCartInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.getCartInfo(user.getId());
    }

    @PostMapping
    public CartItemDtoResponse createCartItem(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemDto request) {
        User user = (User) authentication.getPrincipal();
        return cartService.createCartItem(user.getId(), request);
    }

    @PutMapping("/cart-items/{cartItemId}")
    public CartItemDtoResponse updateCartItem(
            Authentication authentication,
            @PathVariable int cartItemId,
            @RequestBody @Valid PutCartItemDto quantity) {
        User user = (User) authentication.getPrincipal();
        return cartService.updateCartItem(user.getId(), cartItemId, quantity);
    }
}
