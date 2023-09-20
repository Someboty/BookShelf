package com.bookshop.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartItemDto {
    @NotNull(message = "bookId can't be null")
    @Min(value = 0, message = "book id can't be negative number")
    @Schema(example = "1")
    private Long bookId;

    @NotNull(message = "quantity can't be null")
    @Min(value = 1, message = "quantity should be greater than 0")
    @Schema(example = "2")
    private int quantity;
}
