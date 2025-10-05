package com.github.martinalexis.course_management.user.service.v1.facade;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDto;

public interface UserUseCase {

    UserResponseDto getById (Long idUser);

}
