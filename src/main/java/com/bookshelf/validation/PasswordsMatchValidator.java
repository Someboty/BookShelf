package com.bookshelf.validation;

import com.bookshelf.dto.user.request.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator
        implements ConstraintValidator<PasswordsMatch, UserRegistrationRequestDto> {

    @Override
    public boolean isValid(
            UserRegistrationRequestDto requestDto,
            ConstraintValidatorContext constraintValidatorContext) {
        return requestDto.getPassword() != null
                && requestDto.getPassword().equals(requestDto.getRepeatPassword());
    }
}
