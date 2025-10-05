package com.github.martinalexis.course_management.course.repository;

import com.github.martinalexis.course_management.course.model.RoleEnum;
import com.github.martinalexis.course_management.course.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByRole(RoleEnum roleEnum);
}
