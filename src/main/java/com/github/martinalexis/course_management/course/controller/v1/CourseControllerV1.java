package com.github.martinalexis.course_management.course.controller.v1;

import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.service.v1.facade.CourseUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/{idCourse}")
    public ResponseEntity<CreateCourseResponseDtoV1> getById(@PathVariable int idCourse) {
        return ResponseEntity.status(HttpStatus.OK).body(courseUseCase.getById(idCourse));
    }

    @PatchMapping("/{idCourse}")
    public ResponseEntity<CreateCourseResponseDtoV1> updateCourse (@PathVariable int idCourse, @Valid @RequestBody CreateCourseRequestDtoV1 request) {
        return ResponseEntity.status(HttpStatus.OK).body(courseUseCase.updateCourse(idCourse, request));
    }

    @DeleteMapping("/{idCourse}")
    public ResponseEntity<CreateCourseResponseDtoV1> deleteCourse(@PathVariable int idCourse) {
        return ResponseEntity.status(HttpStatus.OK).body(courseUseCase.deleteCourse(idCourse));
    }

    @GetMapping
    public ResponseEntity<Page<CreateCourseResponseDtoV1>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idCourses") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<CreateCourseResponseDtoV1> coursesPage = courseUseCase.getAllCourses(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(coursesPage);
    }
}