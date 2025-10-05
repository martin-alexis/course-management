package com.github.martinalexis.course_management.course.service.v1.facade;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDto;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDto;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDto;
import com.github.martinalexis.course_management.course.dto.v1.UpdateCourseRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseUseCase {
    EnrollCourseResponseDto enrollStudentToCourse(int idCourse);

    CreateCourseResponseDto createCourse(CreateCourseRequestDto request);

    Page<CreateCourseResponseDto> getAllCourses(String search, Pageable pageable);

    Page<CreateCourseResponseDto> getTeacherCourses(String search, Pageable pageable);

    Page<CreateCourseResponseDto> getStudentCourses(String search, Pageable pageable);

    CreateCourseResponseDto getById(int idCourse);

    CreateCourseResponseDto updateCourse(int idCourse, UpdateCourseRequestDto request);

    void deleteCourse(int idCourse);

}