package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserRegistrationResponseDto;
import com.bookshop.dto.user.UserRegistrationRoleRequestDto;
import com.bookshop.dto.user.UserRegistrationRoleResponseDto;
import com.bookshop.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toResponse(User user);

    UserRegistrationRoleResponseDto toRegistrationResponse(User user);

    User toEntity(UserRegistrationRequestDto userDto);

    UserRegistrationRequestDto toStandardModel(UserRegistrationRoleRequestDto roleDto);

}
