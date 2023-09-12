package com.bookshop.service;

import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserRegistrationResponseDto;
import com.bookshop.dto.user.UserRegistrationRoleRequestDto;
import com.bookshop.dto.user.UserRegistrationRoleResponseDto;
import com.bookshop.exception.RegistrationException;

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
