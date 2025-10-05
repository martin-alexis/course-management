package com.github.martinalexis.course_management.review.service.v1.facade;

import com.github.martinalexis.course_management.review.dto.v1.CreateReviewRequestDto;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewUseCase {

    CreateReviewResponseDto createReview(Long idCourse, CreateReviewRequestDto request);

    void deleteReview(Long idReview);

    CreateReviewResponseDto getById (Long idReview);

    Page<CreateReviewResponseDto> getAllReviewsByCourse(Long idCourse, Pageable pageable);

}
