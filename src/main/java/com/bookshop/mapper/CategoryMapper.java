package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.category.request.CategoryDtoRequest;
import com.bookshop.dto.category.response.CategoryDto;
import com.bookshop.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toCategory(CategoryDtoRequest request);
}
