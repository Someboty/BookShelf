package com.bookshop.service;

import com.bookshop.dto.book.request.BookSearchParameters;
import com.bookshop.dto.book.request.CreateBookRequestDto;
import com.bookshop.dto.book.response.BookDto;
import com.bookshop.dto.book.response.BookDtoWithoutCategoryIds;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);

    void checkBookById(Long id);
}
