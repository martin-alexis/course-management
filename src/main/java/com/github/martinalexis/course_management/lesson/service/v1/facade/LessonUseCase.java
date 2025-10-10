package com.github.martinalexis.course_management.lesson.service.v1.facade;


import com.github.martinalexis.course_management.lesson.dto.v1.CreateLessonRequestDto;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonResponseDto;
import com.github.martinalexis.course_management.lesson.dto.v1.UpdateLessonRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonUseCase {

    LessonResponseDto createLesson(Long idCourse, CreateLessonRequestDto request);

    Page<LessonResponseDto> getLessonsByCourse(Long idCourse, String search, Pageable pageable);

    LessonResponseDto getById(Long idLesson);

    LessonResponseDto updateLesson(Long idLesson, Long idCourse, UpdateLessonRequestDto request);

    void deleteLesson(Long idLesson, Long idCourse);

}
