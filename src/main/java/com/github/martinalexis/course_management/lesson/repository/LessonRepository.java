package com.github.martinalexis.course_management.lesson.repository;

import com.github.martinalexis.course_management.lesson.model.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LessonRepository extends JpaRepository<LessonModel, Integer> {



}
