package com.bookshop.repository.book;

import com.bookshop.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    @Query(value = "FROM Book b LEFT JOIN FETCH b.categories as c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId, Pageable pageable);

    @Query(value = "FROM Book b LEFT JOIN FETCH b.categories c WHERE b.id = :id")
    Optional<Book> findById(Long id);

    //@Query(value = "FROM Book b LEFT JOIN FETCH b.categories")
    Page<Book> findAll(Pageable pageable);
}
