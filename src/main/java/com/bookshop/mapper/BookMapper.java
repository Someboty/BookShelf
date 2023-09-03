package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.BookDto;
import com.bookshop.dto.CreateBookRequestDto;
import com.bookshop.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);
}
