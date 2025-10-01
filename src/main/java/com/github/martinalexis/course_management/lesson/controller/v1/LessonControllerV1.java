package com.github.martinalexis.course_management.lesson.controller.v1;

import com.github.martinalexis.course_management.auth.exceptions.v1.AuthExceptionJsonExamples;
import com.github.martinalexis.course_management.common.exceptions.GlobalExceptionJsonExamples;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.exception.v1.CoursesExceptionJsonExamples;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonRequestDto;
import com.github.martinalexis.course_management.lesson.dto.v1.LessonResponseDto;
import com.github.martinalexis.course_management.lesson.service.v1.facade.LessonUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/v1/courses/{idCourse}/lessons")
@Tag(name = "Lessons", description = "Endpoints for managing course lessons")
@SecurityRequirement(name = "bearerAuth")
public class LessonControllerV1 {
    private final LessonUseCase lessonUseCase;

    @PostMapping()
    @Operation(
            summary = "Create a new lesson for a course",
            description = "Creates a new lesson and associates it with a specific course. Only the teacher who owns the course can perform this action."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Lesson created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LessonResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data. One or more fields in the request body are invalid.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Validation Error", value = GlobalExceptionJsonExamples.VALIDATION_FAILED_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. A valid JWT Bearer token is required.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The authenticated user does not own the course.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "User Not Owner", value = CoursesExceptionJsonExamples.USER_NOT_OWN_COURSE_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found with the provided ID.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Resource Not Found", value = GlobalExceptionJsonExamples.RESOURCE_NOT_FOUND_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A unique constraint was violated (e.g., a lesson with the same title already exists for this course).",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Unique Constraint Violation", value = GlobalExceptionJsonExamples.UNIQUE_CONSTRAINT_VIOLATION_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE))
            )
    })
    public ResponseEntity<LessonResponseDto> createLesson(@PathVariable int idCourse, @Valid @RequestBody LessonRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonUseCase.createLesson(idCourse, request));
    }

    @GetMapping()
    @Operation(
            summary = "Get all lessons for a course",
            description = "Retrieves a paginated list of all lessons associated with a specific course. Supports searching and sorting."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lessons retrieved successfully."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. A valid JWT Bearer token is required.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found with the provided ID.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Resource Not Found", value = GlobalExceptionJsonExamples.RESOURCE_NOT_FOUND_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE))
            )
    })
    public ResponseEntity<Page<LessonResponseDto>> getLessonsByCourse(
            @PathVariable int idCourse,
            @io.swagger.v3.oas.annotations.Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @io.swagger.v3.oas.annotations.Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size,
            @io.swagger.v3.oas.annotations.Parameter(description = "Field to sort by (e.g., 'title', 'id')")
            @RequestParam(defaultValue = "id") String sortBy,
            @io.swagger.v3.oas.annotations.Parameter(description = "Sort direction (asc or desc)")
            @RequestParam(defaultValue = "asc") String direction,
            @io.swagger.v3.oas.annotations.Parameter(description = "Search term to filter lessons by title or content")
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(direction), sortBy)
        );
        Page<LessonResponseDto> lessons = lessonUseCase.getLessonsByCourse(idCourse, search, pageable);

        return ResponseEntity.ok(lessons);
    }
}