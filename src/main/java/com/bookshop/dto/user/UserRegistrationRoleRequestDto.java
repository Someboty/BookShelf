package com.bookshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserRegistrationRoleRequestDto {
    @Schema(example = "bob@mail.com")
    private String email;

    @Schema(example = "qeTuo[246")
    private String password;

    @Schema(example = "qeTuo[246")
    private String repeatPassword;

    @Schema(example = "Bob")
    private String firstName;

    @Schema(example = "Smith")
    private String lastName;

    @Schema(example = "manager")
    private String role;
}
