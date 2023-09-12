package com.bookshop.dto.book;

public record BookSearchParameters(
        String[] titles,
        String[] authors,
        String[] isbn,
        String[] price) {
}
