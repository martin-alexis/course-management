package com.github.martinalexis.course_management.lesson.controller.v1;

import com.github.martinalexis.course_management.lesson.dto.v1.LessonRequestDto;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonResponseDto;
import com.github.martinalexis.course_management.lesson.service.v1.facade.impl.LessonUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LessonControllerV1 {
    private final LessonUseCase lessonUseCase;

    @PostMapping("/{idCourse}")
    public ResponseEntity<LessonResponseDto> createLesson(@PathVariable int idCourse, @Valid @RequestBody LessonRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonUseCase.createLesson(idCourse, request));
    }
}