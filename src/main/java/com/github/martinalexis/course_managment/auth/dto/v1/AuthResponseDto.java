package com.github.martinalexis.course_managment.auth.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}