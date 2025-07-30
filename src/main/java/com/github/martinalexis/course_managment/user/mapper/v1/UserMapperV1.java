package com.github.martinalexis.course_managment.user.mapper.v1;

import com.github.martinalexis.course_managment.user.dto.v1.UserResponseDtoV1;
import com.github.martinalexis.course_managment.user.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperV1 {

    UserResponseDtoV1 toDto(UserModel user);

    UserModel toEntity(UserResponseDtoV1 dto);

}
