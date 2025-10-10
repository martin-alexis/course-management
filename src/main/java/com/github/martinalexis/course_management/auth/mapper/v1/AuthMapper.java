package com.github.martinalexis.course_management.auth.mapper.v1;

import com.github.martinalexis.course_management.auth.dto.v1.RegisterRequestDto;
import com.github.martinalexis.course_management.user.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserModel registerRequestToEntity(RegisterRequestDto dto);


}
