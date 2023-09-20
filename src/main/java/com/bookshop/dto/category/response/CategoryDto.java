package com.bookshop.dto.category.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}
