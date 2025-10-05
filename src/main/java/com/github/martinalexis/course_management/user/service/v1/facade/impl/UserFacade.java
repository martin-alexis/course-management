package com.github.martinalexis.course_management.user.service.v1.facade.impl;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDto;
import com.github.martinalexis.course_management.user.mapper.v1.UserMapper;
import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.service.v1.UserService;
import com.github.martinalexis.course_management.user.service.v1.facade.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserFacade implements UserUseCase {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto getById(int idUser) {
        UserModel user = userService.getUserById(idUser);
        UserResponseDto response = userMapper.toDto(user);
        return response;
    }

}
