package com.bookshop.dto.user;

import com.bookshop.res.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

public record UserRegistrationRoleResponseDto(
        Long id,

        @Schema(example = "bob@mail.com")
        String email,

        @Schema(example = "Bob")
        String firstName,

        @Schema(example = "Smith")
        String lastName,
        @Schema(example =
            """
            "roles": [
                {
                    "id": 3,
                    "name": "ROLE_ADMIN",
                    "authority": "ROLE_ADMIN"
                },
                {
                    "id": 1,
                    "name": "ROLE_USER",
                    "authority": "ROLE_USER"
                },
                {
                    "id": 2,
                    "name": "ROLE_MANAGER",
                    "authority": "ROLE_MANAGER"
                }
            ]
            """)
        Set<UserRole> roles) {
}
