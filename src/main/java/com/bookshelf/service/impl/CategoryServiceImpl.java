package com.bookshelf.service.impl;

import com.bookshelf.dto.category.request.CategoryDtoRequest;
import com.bookshelf.dto.category.response.CategoryDto;
import com.bookshelf.exception.EntityNotFoundException;
import com.bookshelf.mapper.CategoryMapper;
import com.bookshelf.model.Category;
import com.bookshelf.repository.category.CategoryRepository;
import com.bookshelf.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto).toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryById(id));
    }

    @Override
    public CategoryDto save(CategoryDtoRequest request) {
        return categoryMapper.toDto(
                categoryRepository.save(categoryMapper.toCategory(request)));
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryDtoRequest request) {
        Category category = categoryById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        existsById(id);
        categoryRepository.deleteById(id);
    }

    private Category categoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id));
    }

    private void existsById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find category by id: " + id);
        }
    }
}
