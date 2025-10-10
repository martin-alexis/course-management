package com.github.martinalexis.course_management.auth.exceptions.v1;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Authentication token has expired");
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
