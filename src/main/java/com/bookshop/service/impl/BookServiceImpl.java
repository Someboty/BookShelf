package com.bookshop.service.impl;

import com.bookshop.dto.BookDto;
import com.bookshop.dto.BookSearchParameters;
import com.bookshop.dto.CreateBookRequestDto;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.BookMapper;
import com.bookshop.model.Book;
import com.bookshop.repository.book.BookRepository;
import com.bookshop.repository.book.BookSpecificationBuilder;
import com.bookshop.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(requestDto)));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id)
        ));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book newBook = bookMapper.toModel(requestDto);
        newBook.setId(id);
        return bookMapper.toDto(bookRepository.save(newBook));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters bookSearchParameters) {
        return bookRepository.findAll(bookSpecificationBuilder.build(bookSearchParameters))
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
