package com.bookshop.dto.cart.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CartItemDtoResponse {
    private Long id;
    private Long bookId;
    private int quantity;
}
