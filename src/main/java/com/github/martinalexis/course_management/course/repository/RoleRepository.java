package com.github.martinalexis.course_management.course.repository;

import com.github.martinalexis.course_management.course.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, Integer> {
}
