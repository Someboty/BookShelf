package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.book.BookDto;
import com.bookshop.dto.book.BookDtoWithoutCategoryIds;
import com.bookshop.dto.book.CreateBookRequestDto;
import com.bookshop.model.Book;
import com.bookshop.model.Category;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toEntity(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto bookDto) {
        if (!bookDto.categoryIds().isEmpty()) {
            book.setCategories(bookDto.categoryIds().stream()
                    .map(Category::new)
                    .collect(Collectors.toSet()));
        }
    }
}
