package com.bookshelf.dto.category.request;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@EqualsAndHashCode
public class CategoryDtoRequest {
    @NotNull(message = "name can't be null")
    @Length(min = 1, max = 255, message = "name's length should be between 1 and 255 characters")
    private String name;
    @NotNull(message = "description can't be null")
    @Length(min = 1, max = 255,
            message = "description's length should be between 1 and 255 characters")
    private String description;
}
