package com.bookshelf.dto.book.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BookDto {
    private Long id;
    @Schema(example = "title")
    private String title;
    @Schema(example = "author")
    private String author;
    @Schema(example = "978-1569319017")
    private String isbn;
    @Schema(example = "12.99")
    private BigDecimal price;
    @Schema(example = "some description of the book")
    private String description;
    @Schema(example = "imageHolder.com\\url")
    private String coverImage;
    @Schema(example = "comedy")
    @EqualsAndHashCode.Exclude
    private Set<Long> categoryIds;
}
