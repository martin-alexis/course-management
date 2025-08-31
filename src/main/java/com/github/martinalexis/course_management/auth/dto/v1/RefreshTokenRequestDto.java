package com.github.martinalexis.course_management.auth.dto.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenRequestDto {
    @NotBlank(message = "Refresh token is required")
    @Size(min = 10, message = "Invalid token format")
    private String refreshToken;
}