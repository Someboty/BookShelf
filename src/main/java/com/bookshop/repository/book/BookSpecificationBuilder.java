package com.bookshop.repository.book;

import com.bookshop.dto.book.BookSearchParameters;
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
        if (isParameterPresent(bookSearchParameters.titles())) {
            addSpecification(specification, TITLE_KEY, bookSearchParameters.titles());
        }
        if (isParameterPresent(bookSearchParameters.authors())) {
            addSpecification(specification, AUTHOR_KEY, bookSearchParameters.authors());
        }
        if (isParameterPresent(bookSearchParameters.isbn())) {
            addSpecification(specification, ISBN_KEY, bookSearchParameters.isbn());
        }
        if (isParameterPresent(bookSearchParameters.price())) {
            addSpecification(specification, PRICE_KEY, bookSearchParameters.price());
        }
        return specification;
    }

    private boolean isParameterPresent(String[] param) {
        return param != null && param.length > 0;
    }

    private void addSpecification(Specification<Book> specification, String key, String[] params) {
        specification.and(bookSpecificationProviderManager
                .getSpecificationProvider(key).getSpecification(params));
    }
}
