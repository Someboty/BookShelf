package com.bookshop.service;

import com.bookshop.dto.user.request.UserRegistrationRequestDto;
import com.bookshop.dto.user.response.UserRegistrationResponseDto;
import com.bookshop.dto.user.request.UserRegistrationRoleRequestDto;
import com.bookshop.dto.user.response.UserRegistrationRoleResponseDto;
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
