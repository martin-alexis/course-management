package com.github.martinalexis.course_management.review.repository;

import com.github.martinalexis.course_management.review.model.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewModel, Integer> {
}
