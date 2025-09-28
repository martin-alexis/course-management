package com.github.martinalexis.course_management.lesson.service.v1.facade.impl;

import com.github.martinalexis.course_management.lesson.dto.v1.LessonRequestDto;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LessonFacade implements LessonUseCase{
    @Override
    public LessonResponseDto createLesson(LessonRequestDto request) {
        return null;
    }

    @Override
    public Page<LessonResponseDto> getLessonsByCourse(String search, Pageable pageable) {
        return null;
    }

    @Override
    public LessonResponseDto getById(int idLesson) {
        return null;
    }

    @Override
    public LessonResponseDto updateLesson(int idLesson, LessonRequestDto request) {
        return null;
    }

    @Override
    public LessonResponseDto deleteLesson(int idLesson) {
        return null;
    }
}
