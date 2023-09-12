package com.bookshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRegistrationRoleRequestDto(
        @Schema(example = "bob@mail.com")
        String email,

        @Schema(example = "qeTuo[246")
        String password,

        @Schema(example = "qeTuo[246")
        String repeatPassword,

        @Schema(example = "Bob")
        String firstName,

        @Schema(example = "Smith")
        String lastName,

        @Schema(example = "manager")
        String role) {
}
