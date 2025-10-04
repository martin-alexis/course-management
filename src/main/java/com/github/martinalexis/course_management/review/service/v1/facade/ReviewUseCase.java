package com.github.martinalexis.course_management.review.service.v1.facade;

import com.github.martinalexis.course_management.review.dto.v1.CreateReviewRequestDto;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewResponseDto;

public interface ReviewUseCase {

    CreateReviewResponseDto createReview(int idCourse, CreateReviewRequestDto request);

    void DeleteReview(int idReview);

    CreateReviewResponseDto getById (int idReview);

}
