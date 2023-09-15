package com.bookshop.dto.book;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BookSearchParameters {
    private String[] titles;
    private String[] authors;
    private String[] isbn;
    private String[] price;
}
