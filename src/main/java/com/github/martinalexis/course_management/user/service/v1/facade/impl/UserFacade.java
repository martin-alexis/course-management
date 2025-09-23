package com.github.martinalexis.course_management.user.service.v1.facade.impl;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.user.dto.v1.UserResponseDtoV1;
import com.github.martinalexis.course_management.user.mapper.v1.UserMapperV1;
import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.service.v1.UserServiceV1;
import com.github.martinalexis.course_management.user.service.v1.facade.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserFacade implements UserUseCase {
    private final UserServiceV1 userService;
    private final UserMapperV1 userMapper;

    @Override
    public UserResponseDtoV1 getById(int idUser) {
        UserModel user = userService.getUserById(idUser);
        UserResponseDtoV1 response = userMapper.toDto(user);
        return response;
    }
}
