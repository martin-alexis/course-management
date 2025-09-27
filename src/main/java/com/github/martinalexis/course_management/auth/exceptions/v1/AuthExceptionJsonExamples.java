package com.github.martinalexis.course_management.auth.exceptions.v1;

public class AuthExceptionJsonExamples {
    /**
     * Example response for when Spring Security's authentication manager
     * rejects credentials before any custom logic.
     * Corresponds to: handleBadCredentials(BadCredentialsException ex)
     */
    public static final String BAD_CREDENTIALS_RESPONSE = """
                {
                  "type": "https://example.com/errors/bad-credentials",
                  "title": "Invalid credentials",
                  "status": 401,
                  "detail": "The email or password provided is incorrect.",
                  "instance": "/api/v1/auth/login"
                }
            """;

    /**
     * Example response for a custom invalid credentials check.
     * While similar to BAD_CREDENTIALS_RESPONSE, this might be triggered
     * by a different, more specific logic path in your code.
     * Corresponds to: handleInvalidCredentials(InvalidCredentialsException ex)
     */
    public static final String INVALID_CREDENTIALS_RESPONSE = """
                {
                  "type": "https://example.com/errors/authentication",
                  "title": "Invalid credentials",
                  "status": 401,
                  "detail": "Invalid username or password",
                  "instance": "/api/v1/auth/login"
                }
            """;

    /**
     * Example response for when the main JWT access token has expired.
     * Corresponds to: handleTokenExpired(TokenExpiredException ex)
     */
    public static final String TOKEN_EXPIRED_RESPONSE = """
                {
                  "type": "https://example.com/errors/authentication",
                  "title": "Token expired",
                  "status": 401,
                  "detail": "JWT token has expired",
                  "instance": "/api/v1/courses"
                }
            """;

    /**
     * Example response for when the refresh token itself has expired,
     * forcing the user to log in again.
     * Corresponds to: handleRefreshTokenExpired(RefreshTokenExpiredException ex)
     */
    public static final String REFRESH_TOKEN_EXPIRED_RESPONSE = """
                {
                  "type": "https://example.com/errors/refresh-token-expired",
                  "title": "Refresh token expired",
                  "status": 401,
                  "detail": "Refresh token has expired",
                  "instance": "/api/v1/auth/refresh"
                }
            """;
}

