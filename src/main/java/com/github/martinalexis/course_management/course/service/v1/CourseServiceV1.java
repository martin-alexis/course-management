package com.github.martinalexis.course_management.course.service.v1;

import com.github.martinalexis.course_management.common.exceptions.ResourceNotFoundException;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CourseServiceV1 {

    private final CourseRepository courseRepository;


    public CourseModel saveCourse(CourseModel course) {
        course.setCreatedOn(LocalDateTime.now());
        return courseRepository.save(course);
    }

    public CourseModel findByIdOrThrow(int idCourse) {
        return courseRepository.findById(idCourse)
                .orElseThrow(() -> new ResourceNotFoundException("course", idCourse));
    }
}