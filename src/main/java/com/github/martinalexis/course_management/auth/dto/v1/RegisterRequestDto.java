package com.github.martinalexis.course_management.auth.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @Schema(description = "First name", example = "Alexis")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Last name", example = "Martin")
    @NotBlank(message = "Lastname is required")
    private String lastname;

    @Schema(description = "Email address", example = "alexis@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Account password", example = "StrongP@ssw0rd!")
    @NotBlank(message = "Password is required")
    private String password;
}