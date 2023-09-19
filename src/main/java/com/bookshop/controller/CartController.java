package com.bookshop.controller;

import com.bookshop.dto.cart.CartDto;
import com.bookshop.dto.cart.CartItemDtoResponse;
import com.bookshop.dto.cart.CreateCartItemDto;
import com.bookshop.dto.cart.PutCartItemDto;
import com.bookshop.model.User;
import com.bookshop.res.Openapi;
import com.bookshop.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Carts", description = "Operations related to carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private static final String BAD_REQUEST_EXAMPLE = Openapi.BAD_REQUEST_EXAMPLE;
    private static final String CART_ITEM_NOT_FOUND_EXAMPLE = Openapi.OBJECT_NOT_FOUND_EXAMPLE;

    private final CartService cartService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of cart items retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            )
    })
    @GetMapping
    @Operation(summary = "Get list of all cart items in the user cart",
            description = "Returns a list of cart items")
    public CartDto getCartInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.getCartInfo(user.getId());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Cart item created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Incorrect data was provided to the body",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @Operation(summary = "Create a new cart item",
            description = "Creates a new cart item based on data, provided in the body")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemDtoResponse createCartItem(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemDto request) {
        User user = (User) authentication.getPrincipal();
        return cartService.createCartItem(user.getId(), request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Incorrect data was provided to the body",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @Operation(summary = "Update a cart item by id",
            description = "Updates a cart item, based on data, provided in the body")
    @PutMapping("/cart-items/{cartItemId}")
    public CartItemDtoResponse updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid PutCartItemDto request) {
        User user = (User) authentication.getPrincipal();
        return cartService.updateCartItem(user.getId(), cartItemId, request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Cart item deleted successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Cart item with such id doesn't exists or was previously deleted",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CART_ITEM_NOT_FOUND_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete cart item by id",
            description = "Deletes cart item with certain id")
    public void deleteCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        cartService.removeCartItem(user.getId(), cartItemId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Cart item deleted successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Cart item with such id doesn't exists or was previously deleted",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CART_ITEM_NOT_FOUND_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @DeleteMapping
    @Operation(summary = "Clear cart", description = "Deletes all cart items in user cart")
    public void clearCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cartService.clearCart(user.getId());
    }
}
