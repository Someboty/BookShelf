package com.bookshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserLoginResponseDto {
    @Schema(example = "eyJhbGciOiJIUzUxMiJ9."
            + "eyJzdWIiOiJ2bGFkaWduYXRpdWtAZ21haWwuY29tIiwiaWF0"
            + "IjoxNjk0NDMwMTkyLCJleHAiOjE2OTQ0MzA0OTJ9."
            + "0XJS72DyWMF_O8xZf928uWBwWFxyxo-WOrg_k8k7eLS7Uyo3"
            + "IH55z7kFHf-j-Hkmsr1VXuDIN1o3PgMmcMlSMg")
    private String token;

    public UserLoginResponseDto(String token) {
        this.token = token;
    }
}
