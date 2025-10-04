package com.github.martinalexis.course_management.review.service.v1.facade.impl;

import com.github.martinalexis.course_management.auth.service.v1.AuthService;
import com.github.martinalexis.course_management.course.exception.v1.StudentAlreadyEnrolledException;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.service.v1.CourseServiceV1;
import com.github.martinalexis.course_management.course.service.v1.rules.StudentAlreadyEnrolledRule;
import com.github.martinalexis.course_management.course.service.v1.rules.VerifyStudentEnrolledInCourseRule;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewRequestDto;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewResponseDto;
import com.github.martinalexis.course_management.review.exception.v1.StudentAlreadyReviewedException;
import com.github.martinalexis.course_management.review.mapper.v1.ReviewMapper;
import com.github.martinalexis.course_management.review.model.ReviewModel;
import com.github.martinalexis.course_management.review.service.v1.ReviewService;
import com.github.martinalexis.course_management.review.service.v1.facade.ReviewUseCase;
import com.github.martinalexis.course_management.review.service.v1.rules.StudentAlreadyReviewedInCourseRole;
import com.github.martinalexis.course_management.review.service.v1.rules.VerifyReviewBelongsToUserRule;
import com.github.martinalexis.course_management.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewFacade implements ReviewUseCase {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final AuthService authService;
    private final CourseServiceV1 courseService;
    private final VerifyStudentEnrolledInCourseRule verifyStudentEnrolledInCourseRule;
    private final StudentAlreadyReviewedInCourseRole studentAlreadyEnrolledRule;
    private final VerifyReviewBelongsToUserRule verifyReviewBelongsToUserRule;

    @Override
    public CreateReviewResponseDto createReview(int idCourse, CreateReviewRequestDto request) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel course = courseService.findByIdOrThrow(idCourse);

        verifyStudentEnrolledInCourseRule.execute(currentUser, course);

        ReviewModel newReview = reviewMapper.createReviewRequestToEntity(request);

        studentAlreadyEnrolledRule.execute(newReview);

        ReviewModel savedReview = reviewService.saveReview(newReview, course, currentUser);

        return reviewMapper.createReviewRequestToDto(savedReview);
    }

    @Override
    public void deleteReview(int idReview) {
        UserModel currentUser = authService.getCurrentUser();

        ReviewModel review = reviewService.findByIdOrThrow(idReview);

        verifyStudentEnrolledInCourseRule.execute(currentUser, review.getCourse());

        verifyReviewBelongsToUserRule.execute(review, currentUser);

        reviewService.deleteReview(review);


    }

    @Override
    public CreateReviewResponseDto getById(int idReview) {
        authService.getCurrentUser();

        ReviewModel review = reviewService.findByIdOrThrow(idReview);

        return reviewMapper.createReviewRequestToDto(review);
    }

    @Override
    public Page<CreateReviewResponseDto> getAllReviewsByCourse(int idCourse, Pageable pageable) {
        authService.getCurrentUser();

        CourseModel course = courseService.findByIdOrThrow(idCourse);

        Page<ReviewModel> reviews = reviewService.getReviewsByCourse(course, pageable);

        return reviews.map(review -> {
            CreateReviewResponseDto response = reviewMapper.createReviewRequestToDto(review);
            return response;
        });

    }
}
