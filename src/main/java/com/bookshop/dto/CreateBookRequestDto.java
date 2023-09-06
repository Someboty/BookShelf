package com.bookshop.dto;

import com.bookshop.validation.Isbn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateBookRequestDto(
        @NotNull
        @Size(max = 255)
        String title,
        @NotNull
        @Size(max = 255)
        String author,
        @Isbn
        String isbn,
        @NotNull
        @Min(0)
        BigDecimal price,
        @Size(max = 255)
        String description,
        @Size(max = 255)
        String coverImage) {
}
