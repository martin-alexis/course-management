package com.github.martinalexis.course_management.user.service.v1.facade.impl;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDto;
import com.github.martinalexis.course_management.user.mapper.v1.UserMapper;
import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.service.v1.UserService;
import com.github.martinalexis.course_management.user.service.v1.facade.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Facade implementation for user read use cases.
 *
 * <p>Coordinates calls to the domain service and mapping layer to return
 * API-facing DTOs. This keeps controllers thin and encapsulates mapping
 * concerns.</p>
 *
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class UserFacade implements UserUseCase {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    /**
     * Retrieves a user by id and maps it to {@link UserResponseDto}.
     *
     * @param idUser unique user identifier
     * @return DTO with public user information
     */
    public UserResponseDto getById(Long idUser) {
        UserModel user = userService.getUserById(idUser);
        UserResponseDto response = userMapper.toDto(user);
        return response;
    }

}
