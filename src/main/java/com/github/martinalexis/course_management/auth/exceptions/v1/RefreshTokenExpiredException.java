package com.github.martinalexis.course_management.auth.exceptions.v1;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() {
        super("Refresh token has expired");
    }

    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}

