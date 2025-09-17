package com.github.martinalexis.course_management.course.mapper.v1;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.model.CourseModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapperV1 {
    // Entity -> Response
    CreateCourseRequestDtoV1 toDto(CourseModel course);

    // Request -> Entity
    CourseModel toEntity(CreateCourseResponseDtoV1 dto);
}
