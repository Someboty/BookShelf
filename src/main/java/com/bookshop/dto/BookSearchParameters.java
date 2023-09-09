package com.bookshop.dto;

public record BookSearchParameters(
        String[] titles,
        String[] authors,
        String[] isbn,
        String[] price) {
}
