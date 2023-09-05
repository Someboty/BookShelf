package com.bookshop.repository.book.spec;

import com.bookshop.model.Book;
import com.bookshop.repository.SpecificationProvider;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "price";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> params.length == 1 ? criteriaBuilder.lessThanOrEqualTo(root.get("price"), params[0]) :
                criteriaBuilder.between(root.get("price"),
                        BigDecimal.valueOf(Long.parseLong(params[0]))
                                .min(BigDecimal.valueOf(Long.parseLong(params[1]))),
                        BigDecimal.valueOf(Long.parseLong(params[0]))
                                .max(BigDecimal.valueOf(Long.parseLong(params[1]))));
    }
}
