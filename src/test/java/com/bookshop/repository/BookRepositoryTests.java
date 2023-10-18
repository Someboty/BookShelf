package com.bookshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookshop.model.Book;
import com.bookshop.repository.book.BookRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTests {
    private static final String REMOVE_ALL_BOOKS_AND_CATEGORIES =
            "classpath:database/books/remove-all-books-and-categories.sql";
    private static final String ADD_ONE_BOOK_WITH_CATEGORY =
            "classpath:database/books/add-one-book-with-first-category.sql";
    private static final Pageable STANDART_PAGEABLE = PageRequest.of(0, 20);
    private static final Long CORRECT_ID_ONE = 1L;
    private static final Long CORRECT_ID_TWO = 2L;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find only book with correct category 1")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = REMOVE_ALL_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_OneBookWithCorrectCategory_ReturnsOneBook() {
        int expected = 1;
        
        List<Book> actual = bookRepository.findAllByCategoryId(CORRECT_ID_ONE, STANDART_PAGEABLE);
        
        assertEquals(expected, actual.size());
        assertEquals("The Book", actual.get(0).getTitle());
    }

    @Test
    @DisplayName("Looking for only book with incorrect category 2")
    @Sql(scripts = REMOVE_ALL_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = REMOVE_ALL_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_OneBookWithIncorrectCategory_ReturnsEmptyList() {
        int expected = 0;
        
        List<Book> actual = bookRepository.findAllByCategoryId(CORRECT_ID_TWO, STANDART_PAGEABLE);
        
        assertEquals(expected, actual.size());
    }

    @Test
    @DisplayName("Find few books with correct category 1")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            "classpath:database/books/add-three-books-with-first-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = REMOVE_ALL_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ManyBooksWithCorrectCategory_ReturnsListOfBooks() {
        int expected = 3;
        
        List<Book> actual = bookRepository.findAllByCategoryId(CORRECT_ID_ONE, STANDART_PAGEABLE);

        assertEquals(expected, actual.size());
        assertEquals("The First Book", actual.get(0).getTitle());
        assertEquals("The Second Book", actual.get(1).getTitle());
        assertEquals("The Third Book", actual.get(2).getTitle());
    }

    @Test
    @DisplayName("Looking between few books with incorrect category 2")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            "classpath:database/books/add-three-books-with-first-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = REMOVE_ALL_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ManyBooksWithInCorrectCategory_ReturnsEmptyList() {
        int expected = 0;
        
        List<Book> actual = bookRepository.findAllByCategoryId(CORRECT_ID_TWO, STANDART_PAGEABLE);

        assertEquals(expected, actual.size());
    }

    @Test
    @DisplayName("Find few books within different books by category 1")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            "classpath:database/books/add-ten-books-with-different-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = REMOVE_ALL_BOOKS_AND_CATEGORIES,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ManyBooksWithDifferentCategories_ReturnsListOfBooks() {
        int expected = 4;
        
        List<Book> actual = bookRepository.findAllByCategoryId(CORRECT_ID_TWO, STANDART_PAGEABLE);

        assertEquals(expected, actual.size());
        assertEquals("The Forth Book", actual.get(0).getTitle());
        assertEquals("The Fifth Book", actual.get(1).getTitle());
        assertEquals("The Sixth Book", actual.get(2).getTitle());
        assertEquals("The Seventh Book", actual.get(3).getTitle());
    }
}
