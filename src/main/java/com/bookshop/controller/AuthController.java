package com.bookshop.controller;

import com.bookshop.dto.user.UserLoginRequestDto;
import com.bookshop.dto.user.UserLoginResponseDto;
import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserRegistrationResponseDto;
import com.bookshop.exception.RegistrationException;
import com.bookshop.security.AuthenticationService;
import com.bookshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private static final String CODE_200 = "200";
    private static final String TOKEN_GENERATED_DESCRIPTION =
            "JWT token was generated successfully";
    private static final String REGISTRATION_SUCCESSFUL =
            "registration was successful";
    private static final String CODE_204 = "204";
    private static final String DELETED_USER_DESCRIPTION = "User deleted successfully";
    private static final String ROLE_SET = "role was set to the user";
    private static final String CODE_401 = "401";
    private static final String CODE_401_DESCRIPTION =
            "User should be authenticated to do that operation";
    private static final String CODE_403 = "403";
    private static final String CODE_403_DESCRIPTION =
            "Only users with role \"ADMIN\" can do such operation";
    private static final String CODE_500 = "500";
    private static final String CODE_500_DESCRIPTION = "Internal server error";
    private static final String INCORRECT_DATA =
            """
                {
                    "timestamp": "*time of event*",
                    "status": "INTERNAL_SERVER_ERROR",
                    "errors": [
                        "Incorrect user data"
                    ]
                }
            """;
    private static final String MEDIA_TYPE = "application/json";

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_200, description = TOKEN_GENERATED_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = INCORRECT_DATA)}
                    )}
            ),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = INCORRECT_DATA)}
                    )}
            )
    })
    @Operation(summary = "login method",
            description = "returns you a JWT token if authentication was successful")
    @PostMapping("/login")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_200, description = REGISTRATION_SUCCESSFUL),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = INCORRECT_DATA)}
                    )}
            )
    })
    @Operation(summary = "register", description = "register a new user in system")
    @PostMapping("/register")
    public UserRegistrationResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_200, description = ROLE_SET),
            @ApiResponse(responseCode = CODE_401, description = CODE_401_DESCRIPTION),
            @ApiResponse(responseCode = CODE_403, description = CODE_403_DESCRIPTION),
            @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
                    content = {@Content(mediaType = MEDIA_TYPE,
                            examples = {@ExampleObject(value = INCORRECT_DATA)}
                            )}
                    )
    })
    @Operation(summary = "set user's role",
            description = "operation only for users with \"ADMIN\" role. works in both ways")
    @PutMapping("/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserRegistrationResponseDto setAsRole(
            @RequestBody String query) throws RegistrationException {
        return userService.setAsRole(query);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = CODE_204, description = DELETED_USER_DESCRIPTION),
            @ApiResponse(responseCode = CODE_401, description = CODE_401_DESCRIPTION),
            @ApiResponse(responseCode = CODE_403, description = CODE_403_DESCRIPTION),
            @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
                    content = {@Content(mediaType = MEDIA_TYPE,
                            examples = {@ExampleObject(value = INCORRECT_DATA)}
                            )}
                    ),
    })
    @DeleteMapping("")
    @Operation(summary = "Delete a user by email",
            description = "Deletes an user with certain email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestBody String email) {
        userService.deleteByEmail(email);
    }
}
