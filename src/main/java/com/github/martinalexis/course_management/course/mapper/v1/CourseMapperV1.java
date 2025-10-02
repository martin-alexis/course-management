package com.github.martinalexis.course_management.course.mapper.v1;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.UpdateCourseRequestDto;
import com.github.martinalexis.course_management.course.model.CourseModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapperV1 {
    // Request -> Entity
    CourseModel toEntity(CreateCourseRequestDtoV1 dto);

    CourseModel updateCourseRequestToEntity(UpdateCourseRequestDto dto);

    // Entity -> Response
    CreateCourseResponseDtoV1 toResponse(CourseModel course);

    EnrollCourseResponseDtoV1 toEnrollResponse(CourseModel course);
}
