package com.bookshop.dto.cart.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CartItemDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "1")
    private Long bookId;
    @Schema(example = "The Book")
    private String bookTitle;
    @Schema(example = "2")
    private int quantity;
}
