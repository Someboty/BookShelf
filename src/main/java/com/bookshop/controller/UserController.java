package com.bookshop.controller;

import com.bookshop.dto.user.UserRegistrationRoleRequestDto;
import com.bookshop.dto.user.UserRegistrationRoleResponseDto;
import com.bookshop.exception.RegistrationException;
import com.bookshop.res.Openapi;
import com.bookshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Users", description = "Controller for operations with users")
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {
    private static final String INCORRECT_DATA = Openapi.INCORRECT_DATA;

    private final UserService userService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
            description = "user with defined roles was successfully created"),
        @ApiResponse(responseCode = "401",
            description = "User should be authenticated to do that operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "403",
            description = "Only users with role \"ADMIN\" can do such operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = INCORRECT_DATA)}
                    )}
            )
    })
    @Operation(summary = "Create a new user with defined roles",
            description = "Creates a new user based on data, "
                    + "provided in the body (standard user + role)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationRoleResponseDto registerWithRole(
            @RequestBody UserRegistrationRoleRequestDto request)
            throws RegistrationException {
        return userService.registerWithRole(request);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "role was set to the user"),
        @ApiResponse(responseCode = "401",
            description = "User should be authenticated to do that operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "403",
            description = "Only users with role \"ADMIN\" can do such operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = INCORRECT_DATA)}
                    )}
            )
    })
    @Operation(summary = "set user's role",
            description = "operation only for users with \"ADMIN\" role. works in both ways")
    @PutMapping("/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserRegistrationRoleResponseDto setAsRole(
            @RequestBody @Schema(example = "manager") String query) throws RegistrationException {
        return userService.setAsRole(query);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "401",
            description = "User should be authenticated to do that operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "403",
            description = "Only users with role \"ADMIN\" can do such operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = INCORRECT_DATA)}
                    )}
            ),
    })
    @DeleteMapping("")
    @Operation(summary = "Delete a user by email",
            description = "Deletes an user with certain email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestBody @Schema(example = "bob@mail.com") String email) {
        userService.deleteByEmail(email);
    }
}
