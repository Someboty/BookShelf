package com.bookshop.service.impl;

import com.bookshop.dto.book.BookDto;
import com.bookshop.dto.book.BookDtoWithoutCategoryIds;
import com.bookshop.dto.book.BookSearchParameters;
import com.bookshop.dto.book.CreateBookRequestDto;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.BookMapper;
import com.bookshop.model.Book;
import com.bookshop.repository.book.BookRepository;
import com.bookshop.repository.book.BookSpecificationBuilder;
import com.bookshop.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book entity = bookMapper.toEntity(requestDto);
        return bookMapper.toDto(bookRepository.save(entity));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookById(id));
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookById(id);
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());
        bookMapper.setCategories(book, requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookById(id);
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        return bookRepository.findAll(bookSpecificationBuilder.build(bookSearchParameters),
                        pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoryId(id, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    private Book bookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
    }
}
