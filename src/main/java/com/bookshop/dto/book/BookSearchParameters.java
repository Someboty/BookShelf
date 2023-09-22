package com.bookshop.dto.book;

import lombok.Data;

@Data
public class BookSearchParameters {
    private String[] titles;
    private String[] authors;
    private String[] isbn;
    private String[] price;
}
