package com.github.martinalexis.course_management.lesson.service.v1;

import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.lesson.model.LessonModel;
import com.github.martinalexis.course_management.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
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
}
