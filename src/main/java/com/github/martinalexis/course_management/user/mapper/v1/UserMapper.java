package com.github.martinalexis.course_management.user.mapper.v1;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDto;
import com.github.martinalexis.course_management.user.model.UserModel;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between {@link UserModel} entities and
 * {@link UserResponseDto} representations.
 *
 * <p>Used to keep the controller and facade layers agnostic of persistence
 * details and to ensure a consistent response shape.</p>
 *
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a domain entity to its API response DTO.
     *
     * @param user source entity
     * @return DTO projection for outbound responses
     */
    UserResponseDto toDto(UserModel user);

    /**
     * Maps a response DTO back to an entity.
     *
     * <p>Primarily useful for tests or internal conversions; not typically
     * used for persistence as write DTOs differ from read DTOs.</p>
     *
     * @param dto source DTO
     * @return mapped entity
     */
    UserModel toEntity(UserResponseDto dto);

}
