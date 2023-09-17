package com.bookshop.dto.cart;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDtoResponse> cartItems;
}
