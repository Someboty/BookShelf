package com.bookshop.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record BookDtoWithoutCategoryIds(
                Long id,
                @Schema(example = "title")
                String title,
                @Schema(example = "author")
                String author,
                @Schema(example = "978-1569319017")
                String isbn,
                @Schema(example = "12.99")
                BigDecimal price,
                @Schema(example = "some description of the book")
                String description,
                @Schema(example = "imageHolder.com\\url")
                String coverImage) {
}
