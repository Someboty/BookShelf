package com.bookshop.service.impl;

import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserRegistrationResponseDto;
import com.bookshop.dto.user.UserRegistrationRoleRequestDto;
import com.bookshop.dto.user.UserRegistrationRoleResponseDto;
import com.bookshop.exception.RegistrationException;
import com.bookshop.mapper.UserMapper;
import com.bookshop.model.User;
import com.bookshop.model.UserRole;
import com.bookshop.repository.user.UserRepository;
import com.bookshop.repository.user.UserRoleRepository;
import com.bookshop.service.UserService;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final int EMAIL_INDEX = 1;
    private static final int ROLE_INDEX = 2;
    private static final int QUOTES_LENGTH = 1;
    private static final String QUOTES = "\"";
    private static final String QUERY_SEPARATOR = ":";

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Override
    public UserRegistrationResponseDto register(
            UserRegistrationRequestDto request)
            throws RegistrationException {
        if (isExists(request)) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(userRoleRepository.getUserRoleByName(UserRole.RoleName.ROLE_USER)));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserRegistrationRoleResponseDto registerWithRole(UserRegistrationRoleRequestDto request)
            throws RegistrationException {
        UserRegistrationRequestDto userDto = userMapper.toStandardModel(request);
        User user = userMapper.toModel(userDto);
        setRoles(user, request.role());
        return userMapper.toRegistrationResponse(userRepository.save(user));
    }

    @Override
    public UserRegistrationRoleResponseDto setAsRole(String query) throws RegistrationException {
        User user = findByEmail(query);
        String roles = prepareRole(query);
        setRoles(user, roles);
        return userMapper.toRegistrationResponse(userRepository.save(user));
    }

    @Override
    public void deleteByEmail(String email) {
        userRepository.deleteById(findByEmail(email).getId());
    }

    private boolean isExists(UserRegistrationRequestDto request) {
        return userRepository.findByEmail(request.email()).isPresent();
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(prepareEmail(email))
                .orElseThrow(
                        () -> new NoSuchElementException("Can't find user by email " + email));
    }

    private String prepareEmail(String query) {
        String[] values = query.split(QUERY_SEPARATOR);
        return values[EMAIL_INDEX].substring(
                QUOTES_LENGTH,
                values[EMAIL_INDEX].substring(QUOTES_LENGTH).indexOf(QUOTES) + QUOTES_LENGTH);
    }

    private String prepareRole(String query) {
        String[] values = query.split(QUERY_SEPARATOR);
        return values[ROLE_INDEX].substring(
                QUOTES_LENGTH,
                values[ROLE_INDEX].substring(QUOTES_LENGTH).indexOf(QUOTES) + QUOTES_LENGTH);
    }

    private void setRoles(User user, String roles) throws RegistrationException {
        switch (roles) {
            case ("admin") -> user.setRoles(Set.of(userRoleRepository
                            .getUserRoleByName(UserRole.RoleName.ROLE_USER),
                    userRoleRepository.getUserRoleByName(UserRole.RoleName.ROLE_MANAGER),
                    userRoleRepository.getUserRoleByName(UserRole.RoleName.ROLE_ADMIN)));
            case ("manager") -> user.setRoles(Set.of(userRoleRepository
                            .getUserRoleByName(UserRole.RoleName.ROLE_USER),
                    userRoleRepository.getUserRoleByName(UserRole.RoleName.ROLE_MANAGER)));
            case ("user") -> user.setRoles(Set.of(userRoleRepository
                    .getUserRoleByName(UserRole.RoleName.ROLE_USER)));
            default -> throw new RegistrationException("Incorrect role: " + roles);
        }
    }
}
