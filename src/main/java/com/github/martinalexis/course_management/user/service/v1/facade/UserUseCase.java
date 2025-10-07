package com.github.martinalexis.course_management.user.service.v1.facade;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDto;

/**
 * Application use case boundary for user read operations.
 *
 * <p>Decouples web and service layers by exposing a stable contract for
 * retrieving user data as DTOs.</p>
 *
 * @since 1.0
 */
public interface UserUseCase {

    /**
     * Retrieves a user representation by identifier.
     *
     * @param idUser unique user identifier
     * @return a DTO with public user information
     */
    UserResponseDto getById (Long idUser);

}
