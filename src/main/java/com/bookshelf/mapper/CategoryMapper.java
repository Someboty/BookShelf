package com.bookshelf.mapper;

import com.bookshelf.config.MapperConfig;
import com.bookshelf.dto.category.request.CategoryDtoRequest;
import com.bookshelf.dto.category.response.CategoryDto;
import com.bookshelf.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toCategory(CategoryDtoRequest request);
}
