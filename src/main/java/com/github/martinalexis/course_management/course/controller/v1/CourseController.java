package com.github.martinalexis.course_management.course.controller.v1;

import com.github.martinalexis.course_management.common.exceptions.GlobalExceptionJsonExamples;
import com.github.martinalexis.course_management.auth.exceptions.v1.AuthExceptionJsonExamples;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDto;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDto;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDto;
import com.github.martinalexis.course_management.course.dto.v1.UpdateCourseRequestDto;
import com.github.martinalexis.course_management.course.exception.v1.CoursesExceptionJsonExamples;
import com.github.martinalexis.course_management.course.service.v1.facade.CourseUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "Endpoints for managing courses and enrollments")
public class CourseController {
    private final CourseUseCase courseUseCase;

    @PostMapping
    @Operation(summary = "Create a new course", description = "Creates a new course. The authenticated user will be the teacher.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCourseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Validation Error", value = GlobalExceptionJsonExamples.VALIDATION_FAILED_RESPONSE))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))),
            @ApiResponse(
                    responseCode = "409", description = "A unique constraint was violated.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Unique Constraint Violation", value = GlobalExceptionJsonExamples.UNIQUE_CONSTRAINT_VIOLATION_RESPONSE))
            ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE)))
    })
    public ResponseEntity<CreateCourseResponseDto> createCourse(@Valid @RequestBody CreateCourseRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUseCase.createCourse(request));
    }

    @PostMapping("/{idCourse}/enroll")
    @Operation(summary = "Enroll current student in a course", description = "Enrolls the authenticated student into the specified course.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrolled successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnrollCourseResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Resource Not Found", value = GlobalExceptionJsonExamples.RESOURCE_NOT_FOUND_RESPONSE))),
            @ApiResponse(responseCode = "409", description = "Conflict during enrollment", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = {
                    @ExampleObject(name = "Teacher Cannot Enroll", summary = "Teacher tries to enroll in own course", value = CoursesExceptionJsonExamples.TEACHER_CANNOT_ENROLL_RESPONSE),
                    @ExampleObject(name = "Student Already Enrolled", summary = "Student is already in the course", value = CoursesExceptionJsonExamples.STUDENT_ALREADY_ENROLLED_RESPONSE),
                    @ExampleObject(name = "Unique Constraint Violation", summary = "Generic unique constraint violation", value = GlobalExceptionJsonExamples.UNIQUE_CONSTRAINT_VIOLATION_RESPONSE)
            })),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE)))
    })
    public ResponseEntity<EnrollCourseResponseDto> enrollingStudentToCourse(@PathVariable int idCourse) {
        return ResponseEntity.status(HttpStatus.OK).body(courseUseCase.enrollStudentToCourse(idCourse));
    }

    @GetMapping("/{idCourse}")
    @Operation(summary = "Get a course by ID", description = "Retrieves the details of a specific course.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCourseResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Resource Not Found", value = GlobalExceptionJsonExamples.RESOURCE_NOT_FOUND_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE)))
    })
    public ResponseEntity<CreateCourseResponseDto> getById(@PathVariable int idCourse) {
        return ResponseEntity.status(HttpStatus.OK).body(courseUseCase.getById(idCourse));
    }

    @PatchMapping("/{idCourse}")
    @Operation(summary = "Update a course", description = "Updates the details of a course. Only the teacher who owns the course can perform this action.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCourseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Validation Error", value = GlobalExceptionJsonExamples.VALIDATION_FAILED_RESPONSE))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "User Not Owner", value = CoursesExceptionJsonExamples.USER_NOT_OWN_COURSE_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Resource Not Found", value = GlobalExceptionJsonExamples.RESOURCE_NOT_FOUND_RESPONSE))),
            @ApiResponse(
                    responseCode = "409", description = "A unique constraint was violated (e.g., course name already exists).",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Unique Constraint Violation", value = GlobalExceptionJsonExamples.UNIQUE_CONSTRAINT_VIOLATION_RESPONSE))
            ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE)))
    })
    public ResponseEntity<CreateCourseResponseDto> updateCourse(@PathVariable int idCourse, @Valid @RequestBody UpdateCourseRequestDto request) {
        return ResponseEntity.status(HttpStatus.OK).body(courseUseCase.updateCourse(idCourse, request));
    }

    @DeleteMapping("/{idCourse}")
    @Operation(summary = "Delete a course", description = "Deletes a course. Only the teacher who owns the course can perform this action.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "User Not Owner", value = CoursesExceptionJsonExamples.USER_NOT_OWN_COURSE_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Resource Not Found", value = GlobalExceptionJsonExamples.RESOURCE_NOT_FOUND_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE)))
    })
    public ResponseEntity<Void> deleteCourse(@PathVariable int idCourse) {
        courseUseCase.deleteCourse(idCourse);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @Operation(summary = "Get all available courses", description = "Retrieves a paginated list of all courses. Can be filtered by a search term.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE)))
    })
    public ResponseEntity<Page<CreateCourseResponseDto>> getAllCourses(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by (e.g., 'title', 'idCourses')") @RequestParam(defaultValue = "idCourses") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Search term to filter courses by title or description") @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<CreateCourseResponseDto> coursesPage = courseUseCase.getAllCourses(search, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(coursesPage);
    }

    @GetMapping("/teacher")
    @Operation(summary = "Get courses taught by the current user", description = "Retrieves a paginated list of courses where the authenticated user is the teacher.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE)))
    })
    public ResponseEntity<Page<CreateCourseResponseDto>> getTeacherCourses(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by (e.g., 'title', 'idCourses')") @RequestParam(defaultValue = "idCourses") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Search term to filter courses by title or description") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );
        Page<CreateCourseResponseDto> courses = courseUseCase.getTeacherCourses(search, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @GetMapping("/student")
    @Operation(summary = "Get courses the current user is enrolled in", description = "Retrieves a paginated list of courses where the authenticated user is enrolled as a student.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE)))
    })
    public ResponseEntity<Page<CreateCourseResponseDto>> getStudentCourses(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by (e.g., 'title', 'idCourses')") @RequestParam(defaultValue = "idCourses") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String direction,
            @Parameter(description = "Search term to filter courses by title or description") @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );
        Page<CreateCourseResponseDto> courses = courseUseCase.getStudentCourses(search, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }


}