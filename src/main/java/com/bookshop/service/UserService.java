package com.bookshop.service;

import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserRegistrationResponseDto;
import com.bookshop.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(
            UserRegistrationRequestDto request)
            throws RegistrationException;

    UserRegistrationResponseDto setAsRole(
            String query) throws RegistrationException;

    void deleteByEmail(String email);
}
