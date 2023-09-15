package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.category.CategoryDto;
import com.bookshop.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toCategory(CategoryDto categoryDto);
}
