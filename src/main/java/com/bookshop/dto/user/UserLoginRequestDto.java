package com.bookshop.dto.user;

import jakarta.validation.constraints.NotNull;

public class UserLoginRequestDto {
    @NotNull
    String email;
    @NotNull
    String password;
}
