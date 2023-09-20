package com.bookshop.dto.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
