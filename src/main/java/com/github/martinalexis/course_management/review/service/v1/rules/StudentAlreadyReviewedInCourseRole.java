package com.github.martinalexis.course_management.review.service.v1.rules;

import com.github.martinalexis.course_management.course.exception.v1.StudentAlreadyEnrolledException;
import com.github.martinalexis.course_management.review.model.ReviewModel;
import com.github.martinalexis.course_management.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudentAlreadyReviewedInCourseRole {
    private final ReviewRepository reviewRepository;

    public void execute(ReviewModel review) {
        reviewRepository.findByUserAndCourse(review.getUser(), review.getCourse())
                .ifPresent(r -> {
                    throw new StudentAlreadyEnrolledException();
                });
    }

}
