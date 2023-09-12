package com.bookshop.dto.user;

import com.bookshop.validation.PasswordsMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@PasswordsMatch
public record UserRegistrationRequestDto(
        @NotNull(message = "email can't be null, should be set")
        @Size(min = 4, max = 50, message = "email should be between 4 and 50 characters")
        @Email
        @Schema(example = "bob@mail.com")
        String email,

        @NotNull(message = "password can't be null, should be set")
        @Size(min = 6, max = 100, message = "password should be between 6 and 100 characters")
        @Schema(example = "qeTuo[246")
        String password,

        @NotNull(message = "password can't be null, should be set")
        @Size(min = 6, max = 100, message = "password should be between 6 and 100 characters")
        @Schema(example = "qeTuo[246")
        String repeatPassword,

        @NotNull(message = "firstname can't be null, should be set")
        @Size(min = 2, max = 100, message = "name should be between 2 and 100 characters")
        @Schema(example = "Bob")
        String firstName,

        @NotNull(message = "lastname can't be null, should be set")
        @Size(min = 2, max = 100, message = "lastname should be between 2 and 100 characters")
        @Schema(example = "Smith")
        String lastName,

        @Schema(example = "Ukraine, Kyiv, Maidan Nezalezhnosti")
        @Size(min = 10, max = 255,
                message = "shipping address should be between 10 and 255 characters")
        String shippingAddress) {
}
