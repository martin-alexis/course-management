package com.github.martinalexis.course_management.user.service.v1.facade;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.user.dto.v1.UserResponseDtoV1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserUseCase {

    UserResponseDtoV1 getById (int idUser);

}
