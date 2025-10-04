package com.github.martinalexis.course_management.review.service.v1;


import com.github.martinalexis.course_management.common.exceptions.ResourceNotFoundException;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.review.model.ReviewModel;
import com.github.martinalexis.course_management.review.repository.ReviewRepository;
import com.github.martinalexis.course_management.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewModel saveReview(ReviewModel newReview, CourseModel course, UserModel user) {
        newReview.setCourse(course);
        newReview.setUser(user);
        newReview.setCreatedOn(LocalDateTime.now());
        return reviewRepository.save(newReview);
        }

    public void deleteReview(ReviewModel review) {
        reviewRepository.delete(review);

    }
    public ReviewModel findByIdOrThrow(int idReview) {
        return reviewRepository.findById(idReview).orElseThrow(() -> new ResourceNotFoundException("Review", idReview));
    }

    public Page<ReviewModel> getReviewsByCourse(CourseModel course, Pageable pageable) {
        return reviewRepository.findByCourse(course, pageable);
    }

    public double calculateAverageRating(List<ReviewModel> reviews) {
        return reviews.stream()
                .mapToDouble(ReviewModel::getScore)
                .average()
                .orElse(0.0);
    }



}
