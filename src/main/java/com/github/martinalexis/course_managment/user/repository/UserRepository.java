package com.github.martinalexis.course_managment.user.repository;

import com.github.martinalexis.course_managment.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByEmail(String email);

}
