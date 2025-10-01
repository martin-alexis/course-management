package com.github.martinalexis.course_management.lesson.repository;

import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.lesson.model.LessonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LessonRepository extends JpaRepository<LessonModel, Integer> {
    @Query("""
       SELECT l
       FROM LessonModel l
       WHERE l.course = :course
         AND (:search IS NULL OR :search = '' 
              OR LOWER(l.title) LIKE LOWER(CONCAT('%', :search, '%')) 
              OR LOWER(l.description) LIKE LOWER(CONCAT('%', :search, '%')))
       """)
    Page<LessonModel> findLessonsByCourseAndFilters(
            @Param("course") CourseModel course,
            @Param("search") String search,
            Pageable pageable
    );





}
