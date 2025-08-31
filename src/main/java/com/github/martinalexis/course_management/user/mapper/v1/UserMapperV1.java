package com.github.martinalexis.course_management.user.mapper.v1;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDtoV1;
import com.github.martinalexis.course_management.user.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperV1 {

    UserResponseDtoV1 toDto(UserModel user);

    UserModel toEntity(UserResponseDtoV1 dto);

}
