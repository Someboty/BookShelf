package com.bookshelf.service.impl;

import com.bookshelf.dto.book.request.BookSearchParameters;
import com.bookshelf.dto.book.request.CreateBookRequestDto;
import com.bookshelf.dto.book.response.BookDto;
import com.bookshelf.dto.book.response.BookDtoWithoutCategoryIds;
import com.bookshelf.exception.EntityNotFoundException;
import com.bookshelf.mapper.BookMapper;
import com.bookshelf.model.Book;
import com.bookshelf.repository.book.BookRepository;
import com.bookshelf.repository.book.BookSpecificationBuilder;
import com.bookshelf.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return bookRepository.findAllWithCategories(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookById(id));
    }

    @Override
    @Transactional
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
        checkBookById(id);
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
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    @Override
    public void checkBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find book by id: " + id);
        }
    }

    private Book bookById(Long id) {
        return bookRepository.findByIdWithCategories(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
    }
}
