package com.github.martinalexis.course_management.lesson.mapper.v1;

import com.github.martinalexis.course_management.lesson.dto.v1.CreateLessonRequestDto;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonResponseDto;
import com.github.martinalexis.course_management.lesson.dto.v1.UpdateLessonRequestDto;
import com.github.martinalexis.course_management.lesson.model.LessonModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    // Request -> Entity
    LessonModel createLessonRequestTtoEntity(CreateLessonRequestDto dto);

    LessonModel updateLessonRequestToEntity(UpdateLessonRequestDto dto);

    // Entity -> Response
    LessonResponseDto lessonEntityToResponse(LessonModel entity);
}
