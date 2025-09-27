package com.github.martinalexis.course_management.course.repository;

import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CourseRepository extends JpaRepository<CourseModel, Integer> {
    Page<CourseModel> findAll(Pageable pageable);

    Page<CourseModel> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name, String description, Pageable pageable
    );

    @Query("""
                SELECT c
                FROM CourseModel c
                JOIN c.userCourses uc
                WHERE uc.user.idUser = :userId
                  AND uc.role.role = :role
                  AND (:search IS NULL OR :search = '' 
                       OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) 
                       OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<CourseModel> findCoursesWithRoleAndSearch(
            @Param("userId") int userId,
            @Param("role") RoleEnum role,
            @Param("search") String search,
            Pageable pageable
    );


}
