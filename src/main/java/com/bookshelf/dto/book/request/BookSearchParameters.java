package com.bookshelf.dto.book.request;

import lombok.Data;

@Data
public class BookSearchParameters {
    private String[] titles;
    private String[] authors;
    private String[] isbn;
    private String[] price;
}
