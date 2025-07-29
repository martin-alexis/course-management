package com.github.martinalexis.course_managment.user.repository;

import com.github.martinalexis.course_managment.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
}
