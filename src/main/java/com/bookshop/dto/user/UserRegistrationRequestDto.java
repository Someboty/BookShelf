package com.bookshop.dto.user;

import jakarta.validation.constraints.NotNull;

public record UserRegistrationRequestDto(
        @NotNull
        String email,
        String password,
        String repeatPassword,
        @NotNull
        String firstName,
        @NotNull
        String lastName,
        String shippingAddress) {
}
