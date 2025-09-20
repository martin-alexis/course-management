package com.github.martinalexis.course_management.course.controller.v1;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.service.v1.facade.CourseUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseControllerV1 {
    private final CourseUseCase courseUseCase;

    @PostMapping
    public ResponseEntity<CreateCourseResponseDtoV1> createCourse(@Valid @RequestBody CreateCourseRequestDtoV1 request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUseCase.createCourse(request));
    }

    @PostMapping("/{idCourse}/enroll")
    public ResponseEntity<EnrollCourseResponseDtoV1> enrollingStudentToCourse(@PathVariable int idCourse) {
        return ResponseEntity.status(HttpStatus.OK).body(courseUseCase.enrollStudentToCourse(idCourse));
    }
}
