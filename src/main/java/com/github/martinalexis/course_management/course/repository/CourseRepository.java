package com.github.martinalexis.course_management.course.repository;

import com.github.martinalexis.course_management.course.model.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CourseRepository extends JpaRepository<CourseModel, Integer> {
    Page<CourseModel> findAll(Pageable pageable);

    Page<CourseModel> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name, String description, Pageable pageable
    );



}
