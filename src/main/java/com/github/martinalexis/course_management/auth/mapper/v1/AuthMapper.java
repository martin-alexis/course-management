package com.github.martinalexis.course_management.auth.mapper.v1;

import com.github.martinalexis.course_management.auth.dto.v1.AuthRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.AuthResponseDto;
import com.github.martinalexis.course_management.auth.dto.v1.RefreshTokenRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.RegisterRequestDto;
import com.github.martinalexis.course_management.user.dto.v1.UserResponseDtoV1;
import com.github.martinalexis.course_management.user.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserModel registerRequestToEntity(RegisterRequestDto dto);


}
