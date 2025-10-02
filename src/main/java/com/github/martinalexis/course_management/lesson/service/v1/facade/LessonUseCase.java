package com.github.martinalexis.course_management.lesson.service.v1.facade;


import com.github.martinalexis.course_management.lesson.dto.v1.CreateLessonRequestDto;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonResponseDto;
import com.github.martinalexis.course_management.lesson.dto.v1.UpdateLessonRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonUseCase {

    LessonResponseDto createLesson(int idCourse, CreateLessonRequestDto request);

    Page<LessonResponseDto> getLessonsByCourse(int idCourse, String search, Pageable pageable);

    LessonResponseDto getById(int idLesson);

    LessonResponseDto updateLesson(int idLesson, int idCourse, UpdateLessonRequestDto request);

    LessonResponseDto deleteLesson(int idLesson, int idCourse);

}
