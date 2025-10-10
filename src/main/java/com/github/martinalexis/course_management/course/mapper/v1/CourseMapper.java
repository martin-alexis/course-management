package com.github.martinalexis.course_management.course.mapper.v1;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDto;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDto;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDto;
import com.github.martinalexis.course_management.course.dto.v1.UpdateCourseRequestDto;
import com.github.martinalexis.course_management.course.model.CourseModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    // Request -> Entity
    CourseModel toEntity(CreateCourseRequestDto dto);

    CourseModel updateCourseRequestToEntity(UpdateCourseRequestDto dto);

    // Entity -> Response
    CreateCourseResponseDto toResponse(CourseModel course);

    EnrollCourseResponseDto toEnrollResponse(CourseModel course);
}
