package com.github.martinalexis.course_managment.users.repository;

import com.github.martinalexis.course_managment.users.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
}
