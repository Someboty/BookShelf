package com.bookshelf.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRoleRequestDto {
    @Email
    @NotNull(message = "email can't be null, should be set")
    @Schema(example = "bob@mail.com")
    @Size(min = 4, max = 50, message = "email should be between 4 and 50 characters")
    private String email;

    @NotNull(message = "password can't be null, should be set")
    @Schema(example = "qeTuo[246")
    @Size(min = 6, max = 100, message = "password should be between 6 and 100 characters")
    private String password;

    @NotNull(message = "password can't be null, should be set")
    @Schema(example = "qeTuo[246")
    @Size(min = 6, max = 100, message = "password should be between 6 and 100 characters")
    private String repeatPassword;

    @NotNull(message = "first name can't be null, should be set")
    @Schema(example = "Bob")
    @Size(min = 2, max = 50, message = "first name should be between 2 and 50 characters")
    private String firstName;

    @NotNull(message = "last name can't be null, should be set")
    @Schema(example = "Smith")
    @Size(min = 2, max = 50, message = "last name should be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "role can't be null, should be set")
    @Schema(example = "manager")
    @Size(min = 4, max = 50, message = "role should be between 4 and 50 characters")
    private String role;
}
