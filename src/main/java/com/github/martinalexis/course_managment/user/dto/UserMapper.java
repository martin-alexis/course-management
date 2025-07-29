package com.github.martinalexis.course_managment.user.dto;

import com.github.martinalexis.course_managment.user.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(UserModel user);

    UserModel toEntity(UserResponseDto dto);

}
