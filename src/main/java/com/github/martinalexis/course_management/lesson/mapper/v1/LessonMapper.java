package com.github.martinalexis.course_management.lesson.mapper.v1;

import com.github.martinalexis.course_management.lesson.dto.v1.LessonRequestDto;
import com.github.martinalexis.course_management.lesson.model.LessonModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    // Request -> Entity
    LessonModel lessonRequestTtoEntity(LessonRequestDto dto);
    // Entity -> Response
    LessonRequestDto lessonEntityToResponse(LessonModel entity);
}
