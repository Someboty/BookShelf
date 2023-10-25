package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.user.request.UserRegistrationRequestDto;
import com.bookshop.dto.user.request.UserRegistrationRoleRequestDto;
import com.bookshop.dto.user.response.UserRegistrationResponseDto;
import com.bookshop.dto.user.response.UserRegistrationRoleResponseDto;
import com.bookshop.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toResponse(User user);

    UserRegistrationRoleResponseDto toRegistrationResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserRegistrationRequestDto userDto);

    @Mapping(target = "shippingAddress", ignore = true)
    UserRegistrationRequestDto toStandardModel(UserRegistrationRoleRequestDto roleDto);

}
