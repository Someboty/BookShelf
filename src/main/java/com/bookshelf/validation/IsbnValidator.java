package com.bookshelf.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    private static final String PATTERN_OF_ISBN =
            "^(ISBN[-]*(1[03])*[ ]*(: ){0,1})*(([0-9Xx][- ]*){13}|([0-9Xx][- ]*){10})$";

    @Override
    public boolean isValid(String isbn,
                           ConstraintValidatorContext constraintValidatorContext) {
        return isbn != null
                && Pattern.compile(PATTERN_OF_ISBN).matcher(isbn).matches();
    }
}
