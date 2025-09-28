package com.github.martinalexis.course_management.lesson.service.v1;

import com.github.martinalexis.course_management.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
}
