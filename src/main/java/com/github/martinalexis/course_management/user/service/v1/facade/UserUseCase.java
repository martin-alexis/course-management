package com.github.martinalexis.course_management.user.service.v1.facade;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDtoV1;

public interface UserUseCase {

    UserResponseDtoV1 getById (int idUser);


}
