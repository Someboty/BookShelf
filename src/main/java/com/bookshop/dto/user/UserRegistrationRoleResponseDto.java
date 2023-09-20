package com.bookshop.dto.user;

import com.bookshop.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class UserRegistrationRoleResponseDto {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "bob@mail.com")
    private String email;

    @Schema(example = "Bob")
    private String firstName;

    @Schema(example = "Smith")
    private String lastName;

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
    @EqualsAndHashCode.Exclude
    private Set<Role> roles;
}
