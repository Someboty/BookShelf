package com.bookshelf.dto.book.request;

import com.bookshelf.validation.Isbn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class CreateBookRequestDto {
    @NotNull(message = "title can't be null, should be set")
    @Size(min = 1, max = 255, message = "title should be between 1 and 255 characters")
    @Schema(example = "title")
    private String title;

    @NotNull(message = "author can't be null, should be set")
    @Size(min = 1, max = 255, message = "author should be between 1 and 255 characters")
    @Schema(example = "author")
    private String author;

    @Isbn
    @NotNull(message = "ISBN can't be null, should be set")
    @Size(min = 10, max = 17, message = "ISBN should be between 10 and 17 characters")
    @Schema(example = "978-3-16-148410-0")
    private String isbn;

    @NotNull(message = "price can't be null, should be set")
    @Min(value = 0, message = "price can't be less than 0")
    @Schema(example = "12.99")
    private BigDecimal price;

    @Size(max = 255, message = "description should be less than 255 characters")
    @Schema(example = "some description of the book")
    private String description;

    @Size(max = 255, message = "coverImage should be less than 255 characters")
    @Schema(example = "imageHolder.com\\url")
    private String coverImage;

    @EqualsAndHashCode.Exclude
    private Set<Long> categoryIds;
}
