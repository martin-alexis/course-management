package com.github.martinalexis.course_management.review.controller.v1;

import com.github.martinalexis.course_management.auth.exceptions.v1.AuthExceptionJsonExamples;
import com.github.martinalexis.course_management.common.exceptions.GlobalExceptionJsonExamples;
import com.github.martinalexis.course_management.course.exception.v1.CoursesExceptionJsonExamples;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewRequestDto;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewResponseDto;
import com.github.martinalexis.course_management.review.exception.v1.ReviewExceptionJsonExamples;
import com.github.martinalexis.course_management.review.service.v1.facade.ReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Reviews", description = "Endpoints for managing course reviews")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewUseCase reviewUseCase;

    @PostMapping("/courses/{idCourse}/reviews")
    @Operation(
            summary = "Create a new review for a course",
            description = "Creates a new review for a specific course. The user must be authenticated and enrolled as a student in the course to perform this action."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Review created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateReviewResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data. The request body is invalid (e.g., score out of range).",
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
                    description = "Forbidden. The authenticated user is not enrolled as a student in this course.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Not Enrolled as Student", value = CoursesExceptionJsonExamples.STUDENT_ALREADY_ENROLLED_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found with the provided ID.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Resource Not Found", value = GlobalExceptionJsonExamples.RESOURCE_NOT_FOUND_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict. The user has already reviewed this course.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Unique Constraint Violation", value = ReviewExceptionJsonExamples.STUDENT_ALREADY_REVIEWED_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE))
            )
    })
    public ResponseEntity<CreateReviewResponseDto> createReview(
            @Parameter(description = "ID of the course to be reviewed") @PathVariable int idCourse,
            @Valid @RequestBody CreateReviewRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewUseCase.createReview(idCourse, request));
    }

    @DeleteMapping("/reviews/{idReview}")
    public ResponseEntity DeleteReview (@PathVariable int idReview) {
        reviewUseCase.DeleteReview(idReview);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}