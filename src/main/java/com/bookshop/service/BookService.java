package com.bookshop.service;

import com.bookshop.dto.BookDto;
import com.bookshop.dto.BookSearchParameters;
import com.bookshop.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
