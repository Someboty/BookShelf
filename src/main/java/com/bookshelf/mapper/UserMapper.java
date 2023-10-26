package com.bookshelf.mapper;

import com.bookshelf.config.MapperConfig;
import com.bookshelf.dto.user.request.UserRegistrationRequestDto;
import com.bookshelf.dto.user.request.UserRegistrationRoleRequestDto;
import com.bookshelf.dto.user.response.UserRegistrationResponseDto;
import com.bookshelf.dto.user.response.UserRegistrationRoleResponseDto;
import com.bookshelf.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toResponse(User user);

    UserRegistrationRoleResponseDto toRegistrationResponse(User user);

    User toEntity(UserRegistrationRequestDto userDto);

    UserRegistrationRequestDto toStandardModel(UserRegistrationRoleRequestDto roleDto);

}
