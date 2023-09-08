package com.bookshop.service;

import com.bookshop.dto.BookDto;
import com.bookshop.dto.BookSearchParameters;
import com.bookshop.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable);
}
