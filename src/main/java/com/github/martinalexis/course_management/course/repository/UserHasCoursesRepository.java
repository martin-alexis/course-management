package com.github.martinalexis.course_management.course.repository;

import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.UserHasCoursesModel;
import com.github.martinalexis.course_management.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHasCoursesRepository extends JpaRepository<UserHasCoursesModel, Long> {

    Optional<UserHasCoursesModel> findByUserAndCourse(UserModel user, CourseModel course);
}
