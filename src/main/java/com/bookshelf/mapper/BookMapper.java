package com.bookshelf.mapper;

import com.bookshelf.config.MapperConfig;
import com.bookshelf.dto.book.request.CreateBookRequestDto;
import com.bookshelf.dto.book.response.BookDto;
import com.bookshelf.dto.book.response.BookDtoWithoutCategoryIds;
import com.bookshelf.model.Book;
import com.bookshelf.model.Category;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toEntity(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto bookDto) {
        book.setCategories(bookDto.getCategoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet()));
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Book::new)
                .orElse(null);
    }
}
