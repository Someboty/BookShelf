package com.bookshop.dto.cart.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class CartDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "1")
    private Long userId;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CartItemDto> cartItems;
}
