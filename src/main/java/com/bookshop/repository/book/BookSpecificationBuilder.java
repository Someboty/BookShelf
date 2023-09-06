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
    private static final String TITLE_KEY = "title";
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
    private static final String PRICE_KEY = "price";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.titles() != null
                && bookSearchParameters.titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(bookSearchParameters.titles()));
        }
        if (bookSearchParameters.authors() != null
                && bookSearchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(bookSearchParameters.authors()));
        }
        if (bookSearchParameters.isbn() != null
                && bookSearchParameters.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(ISBN_KEY)
                    .getSpecification(bookSearchParameters.isbn()));
        }
        if (bookSearchParameters.price() != null
                && bookSearchParameters.price().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(PRICE_KEY)
                    .getSpecification(bookSearchParameters.price()));
        }
        return specification;
    }
}
