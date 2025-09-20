package com.github.martinalexis.course_management.course.service.v1.facade;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.model.CourseModel;

public interface CourseUseCase {
    EnrollCourseResponseDtoV1 enrollStudentToCourse(int idCourse);
    CreateCourseResponseDtoV1 createCourse(CreateCourseRequestDtoV1 request);
}
