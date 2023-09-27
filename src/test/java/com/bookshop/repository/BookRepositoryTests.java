package com.bookshop.repository;

import com.bookshop.model.Book;
import com.bookshop.repository.book.BookRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

    //@Test
    @DisplayName("Find only book with category 1")
    @Sql(scripts = "classpath:database/books/add-one-book-with-first-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_OneBookWithCorrectCategory_ReturnsOneBook() {
        List<Book> actual = bookRepository.findAllByCategoryId(1L, STANDART_PAGEABLE);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("The Book", actual.get(0).getTitle());
    }

    //@Test
    @DisplayName("Looking for only book with category 2")
    @Sql(scripts = "classpath:database/books/add-one-book-with-first-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_OneBookWithIncorrectCategory_ReturnsEmptyList() {
        List<Book> actual = bookRepository.findAllByCategoryId(2L, STANDART_PAGEABLE);
        Assertions.assertEquals(0, actual.size());
    }
}
