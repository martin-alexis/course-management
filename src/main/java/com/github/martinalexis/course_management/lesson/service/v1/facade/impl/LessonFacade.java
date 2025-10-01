package com.github.martinalexis.course_management.lesson.service.v1.facade.impl;

import com.github.martinalexis.course_management.auth.service.v1.AuthService;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.service.v1.CourseServiceV1;
import com.github.martinalexis.course_management.course.service.v1.rules.VerifyUserOwnCourseRule;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonRequestDto;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonResponseDto;
import com.github.martinalexis.course_management.lesson.mapper.v1.LessonMapper;
import com.github.martinalexis.course_management.lesson.model.LessonModel;
import com.github.martinalexis.course_management.lesson.service.v1.LessonService;
import com.github.martinalexis.course_management.lesson.service.v1.facade.LessonUseCase;
import com.github.martinalexis.course_management.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LessonFacade implements LessonUseCase {
    private final AuthService authService;
    private final LessonService lessonService;
    private final LessonMapper lessonMapper;
    private final CourseServiceV1 courseService;
    private final VerifyUserOwnCourseRule verifyUserOwnCourseRule;

    @Override
    public LessonResponseDto createLesson(int idCourse, LessonRequestDto request) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel course = courseService.findByIdOrThrow(idCourse);

        verifyUserOwnCourseRule.execute(currentUser, course);

        LessonModel newLesson = lessonMapper.lessonRequestTtoEntity(request);

        LessonModel savedLesson = lessonService.saveLesson(newLesson, course);

        return lessonMapper.lessonEntityToResponse(savedLesson);
    }

    @Override
    public Page<LessonResponseDto> getLessonsByCourse(int idCourse, String search, Pageable pageable) {
        authService.getCurrentUser();

        CourseModel course = courseService.findByIdOrThrow(idCourse);

        Page<LessonModel> lessons = lessonService.getLessonsByCourse(course, search, pageable);

        return lessons.map(lesson -> {
            LessonResponseDto response = lessonMapper.lessonEntityToResponse(lesson);
            return response;
        });
    }

    @Override
    public LessonResponseDto getById(int idLesson) {
        authService.getCurrentUser();
        LessonModel lesson = lessonService.findByIdOrThrow(idLesson);
        return lessonMapper.lessonEntityToResponse(lesson);
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
