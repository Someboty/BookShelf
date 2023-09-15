package com.bookshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserLoginRequestDto {
    @NotNull(message = "email can't be null, should be set")
    @Size(min = 4, max = 50, message = "email should be between 4 and 50 characters")
    @Email
    @Schema(example = "bob@mail.com")
    private String email;

    @NotNull(message = "password can't be null, should be set")
    @Size(min = 6, max = 100, message = "password should be between 6 and 100 characters")
    @Schema(example = "qeTuo[246")
    private String password;
}
