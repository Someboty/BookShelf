package com.bookshop.controller;

import com.bookshop.dto.order.OrderDto;
import com.bookshop.dto.order.OrderItemDto;
import com.bookshop.dto.order.ShippingAddressRequestDto;
import com.bookshop.dto.order.StatusRequestDto;
import com.bookshop.model.User;
import com.bookshop.res.Openapi;
import com.bookshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders", description = "Operations related to orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final String BAD_REQUEST_EXAMPLE = Openapi.BAD_REQUEST_EXAMPLE;
    private static final String CART_NOT_FOUND_EXAMPLE = Openapi.OBJECT_NOT_FOUND_EXAMPLE;

    private final OrderService orderService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of orders retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Can't find order by id",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CART_NOT_FOUND_EXAMPLE)}
                            )}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            )
    })
    @GetMapping
    @Operation(summary = "Get user's orders history",
            description = "Get set of user's orders with items")
    public Set<OrderDto> getHistory(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getHistory(user.getId());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Order created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Incorrect data was provided to the body",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Can't find order by id",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CART_NOT_FOUND_EXAMPLE)}
                            )}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @Operation(summary = "Create a new order",
            description = "Creates a new order based on cart and shipping address")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(
            Authentication authentication,
            @RequestBody @Valid ShippingAddressRequestDto request) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user.getId(), request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Incorrect status was provided to the body",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "403",
                    description = "Only users with role \"MANAGER\" can do such operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Can't find order by id",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CART_NOT_FOUND_EXAMPLE)}
                            )}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @Operation(summary = "Update order status by id",
            description = "Allows manager to change order's status by id")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public OrderDto updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid StatusRequestDto request) {
        return orderService.updateStatus(id, request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Order items retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Can't find order by id",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CART_NOT_FOUND_EXAMPLE)}
                            )}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @Operation(summary = "Get order items by order id",
            description = "Get set of all order items by provided order id")
    @GetMapping("/{orderId}/items")
    public Set<OrderItemDto> getAllItems(@PathVariable Long orderId) {
        return orderService.getAllItems(orderId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Order item retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Can't find order or order item by id",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CART_NOT_FOUND_EXAMPLE)}
                            )}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @Operation(summary = "Get order item by order id and item id",
            description = "Get set of all order items by provided order id and item id")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getItemById(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getItemById(orderId, itemId);
    }
}
