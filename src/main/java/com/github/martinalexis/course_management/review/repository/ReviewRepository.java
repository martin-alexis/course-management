package com.github.martinalexis.course_management.review.repository;

import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.review.model.ReviewModel;
import com.github.martinalexis.course_management.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewModel, Integer> {
    Optional<ReviewModel> findByUserAndCourse(UserModel user, CourseModel course);

}
