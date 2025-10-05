package com.github.martinalexis.course_management.user.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResponseDto {

    @Schema(description = "User identifier", example = "12")
    private Integer idUser;
    @Schema(description = "First name", example = "Alexis")
    private String name;
    @Schema(description = "Last name", example = "Martin")
    private String lastname;
    @Schema(description = "Email address", example = "alexis@example.com")
    private String email;

}
