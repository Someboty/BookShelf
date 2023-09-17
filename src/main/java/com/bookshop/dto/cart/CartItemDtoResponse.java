package com.bookshop.dto.cart;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CartItemDtoResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
