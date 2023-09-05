package com.bookshop.repository.book;

import com.bookshop.dto.BookSearchParameters;
import com.bookshop.model.Book;
import com.bookshop.repository.SpecificationBuilder;
import com.bookshop.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.titles() != null
                && bookSearchParameters.titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(bookSearchParameters.titles()));
        }
        if (bookSearchParameters.authors() != null
                && bookSearchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(bookSearchParameters.authors()));
        }
        if (bookSearchParameters.isbn() != null
                && bookSearchParameters.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("isbn")
                    .getSpecification(bookSearchParameters.isbn()));
        }
        if (bookSearchParameters.price() != null
                && bookSearchParameters.price().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("price")
                    .getSpecification(bookSearchParameters.price()));
        }
        return specification;
    }
}
