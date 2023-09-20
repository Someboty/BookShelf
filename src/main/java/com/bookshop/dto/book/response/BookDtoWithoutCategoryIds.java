package com.bookshop.dto.book.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookDtoWithoutCategoryIds {
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
}
