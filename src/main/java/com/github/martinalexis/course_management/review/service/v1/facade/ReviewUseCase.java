package com.github.martinalexis.course_management.review.service.v1.facade;

import com.github.martinalexis.course_management.review.dto.v1.CreateReviewRequestDto;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewUseCase {

    CreateReviewResponseDto createReview(int idCourse, CreateReviewRequestDto request);

    void deleteReview(int idReview);

    CreateReviewResponseDto getById (int idReview);

    Page<CreateReviewResponseDto> getAllReviewsByCourse(int idCourse, Pageable pageable);

}
