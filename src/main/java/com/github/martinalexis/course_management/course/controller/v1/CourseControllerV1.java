package com.github.martinalexis.course_management.course.controller.v1;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.service.v1.CourseServiceV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseControllerV1 {
    private final CourseServiceV1 courseService;

    @PostMapping
    public ResponseEntity<CreateCourseResponseDtoV1> createCourse(@Valid @RequestBody CreateCourseRequestDtoV1 request) {
        CreateCourseResponseDtoV1 course = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

}
