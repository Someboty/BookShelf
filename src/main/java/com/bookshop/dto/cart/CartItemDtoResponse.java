package com.bookshop.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CartItemDtoResponse {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "1")
    private Long bookId;
    @Schema(example = "2")
    private int quantity;
}
