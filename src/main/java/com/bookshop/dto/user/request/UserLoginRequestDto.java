package com.bookshop.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @Email
    @NotNull(message = "email can't be null, should be set")
    @Size(min = 4, max = 50, message = "email should be between 4 and 50 characters")
    @Schema(example = "bob@mail.com")
    private String email;

    @NotNull(message = "password can't be null, should be set")
    @Size(min = 6, max = 100, message = "password should be between 6 and 100 characters")
    @Schema(example = "qeTuo[246")
    private String password;
}
