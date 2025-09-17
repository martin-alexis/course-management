package com.github.martinalexis.course_management.course.repository;

import com.github.martinalexis.course_management.course.model.UserHasCoursesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasCoursesRepository extends JpaRepository<UserHasCoursesModel, Integer> {
}
