package com.bookshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRegistrationResponseDto(
        Long id,

        @Schema(example = "bob@mail.com")
        String email,

        @Schema(example = "Bob")
        String firstName,

        @Schema(example = "Smith")
        String lastName,

        @Schema(example = "Ukraine, Kyiv, Maidan Nezalezhnosti")
        String shippingAddress) {
}
