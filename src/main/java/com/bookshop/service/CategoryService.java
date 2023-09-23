package com.bookshop.service;

import com.bookshop.dto.category.request.CategoryDtoRequest;
import com.bookshop.dto.category.response.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDtoRequest request);

    CategoryDto update(Long id, CategoryDtoRequest request);

    void deleteById(Long id);
}
