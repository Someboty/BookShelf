package com.bookshop.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRegistrationResponseDto {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "bob@mail.com")
    private String email;

    @Schema(example = "Bob")
    private String firstName;

    @Schema(example = "Smith")
    private String lastName;

    @Schema(example = "Ukraine, Kyiv, Maidan Nezalezhnosti")
    private String shippingAddress;
}
