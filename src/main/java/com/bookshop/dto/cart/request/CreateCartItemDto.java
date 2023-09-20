package com.bookshop.dto.cart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CreateCartItemDto {
    @NotNull(message = "bookId can't be null")
    @Min(value = 0, message = "book id can't be negative number")
    private Long bookId;

    @NotNull(message = "quantity can't be null")
    @Min(value = 1, message = "quantity should be greater than 0")
    private int quantity;
}
