package com.github.martinalexis.course_management.lesson.service.v1.facade.impl;


import com.github.martinalexis.course_management.lesson.dto.v1.LessonRequestDto;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonUseCase {

    LessonResponseDto createLesson(LessonRequestDto request);

    Page<LessonResponseDto> getLessonsByCourse(String search, Pageable pageable);

    LessonResponseDto getById(int idLesson);

    LessonResponseDto updateLesson(int idLesson, LessonRequestDto request);

    LessonResponseDto deleteLesson(int idLesson);

}
