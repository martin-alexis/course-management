package com.github.martinalexis.course_management.course.service.v1.facade;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.model.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface CourseUseCase {
    EnrollCourseResponseDtoV1 enrollStudentToCourse(int idCourse);

    CreateCourseResponseDtoV1 createCourse(CreateCourseRequestDtoV1 request);

    Page<CreateCourseResponseDtoV1> getAllCourses(String search, Pageable pageable);

    Page<CreateCourseResponseDtoV1> getTeacherCourses(String search, Pageable pageable);

    Page<CreateCourseResponseDtoV1> getStudentCourses(String search, Pageable pageable);

    CreateCourseResponseDtoV1 getById(int idCourse);

    CreateCourseResponseDtoV1 updateCourse(int idCourse, CreateCourseRequestDtoV1 request);

    CreateCourseResponseDtoV1 deleteCourse(int idCourse);

}