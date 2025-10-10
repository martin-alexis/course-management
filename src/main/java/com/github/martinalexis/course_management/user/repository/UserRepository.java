package com.github.martinalexis.course_management.user.repository;

import com.github.martinalexis.course_management.user.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for {@link UserModel} entities.
 *
 * <p>Provides CRUD operations and user-specific lookup methods.</p>
 *
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<UserModel, Long> {
    /**
     * Finds a user by email address.
     *
     * @param email unique email address
     * @return an {@link Optional} containing the user if found, empty otherwise
     */
    Optional<UserModel> findByEmail(String email);



}
