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
    private static final String INCORRECT_DATA_MESSAGE =
            """
                {
                    "timestamp": "*time of event*",
                    "status": "INTERNAL_SERVER_ERROR",
                    "errors": [
                        "Incorrect user data"
                    ]
                }
            """;

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "JWT token was generated successfully",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = INCORRECT_DATA_MESSAGE)}
                    )}
            ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = INCORRECT_DATA_MESSAGE)}
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
        @ApiResponse(responseCode = "200", description = "registration was successful"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = INCORRECT_DATA_MESSAGE)}
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
            @ApiResponse(responseCode = "200", description = "role was set to the user"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do that operation"),
            @ApiResponse(responseCode = "403",
                    description = "Only users with role \"ADMIN\" can do such operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = INCORRECT_DATA_MESSAGE)}
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
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do that operation"),
            @ApiResponse(responseCode = "403",
                    description = "Only users with role \"ADMIN\" can do such operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = INCORRECT_DATA_MESSAGE)}
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
