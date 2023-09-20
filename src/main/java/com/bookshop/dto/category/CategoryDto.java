package com.bookshop.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Comedy")
    private String name;
    @Schema(example = "Some funny books")
    private String description;
}
