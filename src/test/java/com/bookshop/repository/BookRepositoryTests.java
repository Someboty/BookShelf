package com.bookshop.repository;

import com.bookshop.model.Book;
import com.bookshop.repository.book.BookRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
    private static final Pageable STANDART_PAGEABLE = PageRequest.of(0, 20);

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find only book with correct category 1")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-book-with-first-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_OneBookWithCorrectCategory_ReturnsOneBook() {
        //given
        int expected = 1;
        //when
        List<Book> actual = bookRepository.findAllByCategoryId(1L, STANDART_PAGEABLE);

        //then
        Assertions.assertEquals(expected, actual.size());
        Assertions.assertEquals("The Book", actual.get(0).getTitle());
    }

    @Test
    @DisplayName("Looking for only book with incorrect category 2")
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_OneBookWithIncorrectCategory_ReturnsEmptyList() {
        //given
        int expected = 0;
        //when
        List<Book> actual = bookRepository.findAllByCategoryId(2L, STANDART_PAGEABLE);

        //then
        Assertions.assertEquals(expected, actual.size());
    }

    @Test
    @DisplayName("Find few books with correct category 1")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-three-books-with-first-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ManyBooksWithCorrectCategory_ReturnsListOfBooks() {
        //given
        int expected = 3;
        //when
        List<Book> actual = bookRepository.findAllByCategoryId(1L, STANDART_PAGEABLE);

        //then
        Assertions.assertEquals(expected, actual.size());
        Assertions.assertEquals("The First Book", actual.get(0).getTitle());
        Assertions.assertEquals("The Second Book", actual.get(1).getTitle());
        Assertions.assertEquals("The Third Book", actual.get(2).getTitle());
    }

    @Test
    @DisplayName("Looking between few books with incorrect category 2")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-three-books-with-first-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ManyBooksWithInCorrectCategory_ReturnsEmptyList() {
        //given
        int expected = 0;
        //when
        List<Book> actual = bookRepository.findAllByCategoryId(2L, STANDART_PAGEABLE);

        //then
        Assertions.assertEquals(expected, actual.size());
    }

    @Test
    @DisplayName("Find few books within different books by category 1")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-ten-books-with-different-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ManyBooksWithDifferentCategories_ReturnsListOfBooks() {
        //given
        int expected = 4;
        //when
        List<Book> actual = bookRepository.findAllByCategoryId(2L, STANDART_PAGEABLE);

        //then
        Assertions.assertEquals(expected, actual.size());
        Assertions.assertEquals("The Forth Book", actual.get(0).getTitle());
        Assertions.assertEquals("The Fifth Book", actual.get(1).getTitle());
        Assertions.assertEquals("The Sixth Book", actual.get(2).getTitle());
        Assertions.assertEquals("The Seventh Book", actual.get(3).getTitle());
    }
}
