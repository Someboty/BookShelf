package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.category.request.CategoryDtoRequest;
import com.bookshop.dto.category.response.CategoryDto;
import com.bookshop.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toCategory(CategoryDtoRequest request);
}
