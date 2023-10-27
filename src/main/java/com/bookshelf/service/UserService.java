package com.bookshelf.service;

import com.bookshelf.dto.user.request.UserRegistrationRequestDto;
import com.bookshelf.dto.user.request.UserRegistrationRoleRequestDto;
import com.bookshelf.dto.user.response.UserRegistrationResponseDto;
import com.bookshelf.dto.user.response.UserRegistrationRoleResponseDto;
import com.bookshelf.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(
            UserRegistrationRequestDto request)
            throws RegistrationException;

    UserRegistrationRoleResponseDto registerWithRole(
            UserRegistrationRoleRequestDto request)
            throws RegistrationException;

    UserRegistrationRoleResponseDto setAsRole(
            String query) throws RegistrationException;

    void deleteByEmail(String email);
}
