package com.github.martinalexis.course_management.review.controller.v1;

import com.github.martinalexis.course_management.review.dto.v1.CreateReviewRequestDto;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewResponseDto;
import com.github.martinalexis.course_management.review.service.v1.facade.impl.ReviewFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewFacade reviewFacade;

    @PostMapping("/courses/{idCourse}/reviews")
    public ResponseEntity<CreateReviewResponseDto> createReview(@PathVariable int idCourse, @Valid @RequestBody CreateReviewRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewFacade.createReview(idCourse, request));
    }

}
