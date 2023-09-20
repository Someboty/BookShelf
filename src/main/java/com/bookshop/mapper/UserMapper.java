package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.user.request.UserRegistrationRequestDto;
import com.bookshop.dto.user.request.UserRegistrationRoleRequestDto;
import com.bookshop.dto.user.response.UserRegistrationResponseDto;
import com.bookshop.dto.user.response.UserRegistrationRoleResponseDto;
import com.bookshop.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toResponse(User user);

    UserRegistrationRoleResponseDto toRegistrationResponse(User user);

    User toEntity(UserRegistrationRequestDto userDto);

    UserRegistrationRequestDto toStandardModel(UserRegistrationRoleRequestDto roleDto);

}
