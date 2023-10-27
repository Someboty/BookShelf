package com.bookshelf.dto.order.response;

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
