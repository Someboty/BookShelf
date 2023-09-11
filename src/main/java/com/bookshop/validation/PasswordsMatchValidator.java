package com.bookshop.validation;

import com.bookshop.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator
        implements ConstraintValidator<PasswordsMatch, UserRegistrationRequestDto> {

    @Override
    public boolean isValid(
            UserRegistrationRequestDto requestDto,
            ConstraintValidatorContext constraintValidatorContext) {
        return requestDto.password() != null
                && requestDto.password().equals(requestDto.repeatPassword());
    }
}
