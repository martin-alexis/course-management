package com.github.martinalexis.course_management.lesson.service.v1;

import com.github.martinalexis.course_management.common.exceptions.ResourceNotFoundException;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.RoleEnum;
import com.github.martinalexis.course_management.lesson.model.LessonModel;
import com.github.martinalexis.course_management.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonModel saveLesson(LessonModel newLesson, CourseModel course) {
        newLesson.setCourse(course);
        newLesson.setPublishedOn(LocalDateTime.now());
        return  lessonRepository.save(newLesson);
    }

    public LessonModel deleteLesson (LessonModel lesson) {
        lessonRepository.delete(lesson);
        return lesson;
    }

    public Page<LessonModel> getLessonsByCourse(CourseModel course, String search, Pageable pageable) {
        return lessonRepository.findLessonsByCourseAndFilters(course, search, pageable);
    }

    public LessonModel findByIdOrThrow(int idLesson) {
        return lessonRepository.findById(idLesson)
                .orElseThrow(() -> new ResourceNotFoundException("lesson", idLesson));
    }

}
