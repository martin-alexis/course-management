package com.github.martinalexis.course_management.course.repository;

import com.github.martinalexis.course_management.course.model.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseModel, Integer> {
}
