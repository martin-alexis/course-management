package com.github.martinalexis.course_management.user.mapper.v1;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDto;
import com.github.martinalexis.course_management.user.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(UserModel user);

    UserModel toEntity(UserResponseDto dto);

}
