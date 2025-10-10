package com.github.martinalexis.course_management.user.exception.v1;

/**
 * Centralized JSON example payloads for User module error responses.
 *
 * <p>Used in OpenAPI annotations to document typical error bodies returned by the
 * exception handlers in this module.</p>
 *
 * @since 1.0
 */
public class UserExceptionJsonExamples {
    /**
     * Example response for when a user tries to register with an email
     * that already exists in the system. This is a uniqueness constraint violation.
     * This typically results in a 409 Conflict status.
     * Corresponds to: DuplicateEmailException
     */
    public static final String DUPLICATE_EMAIL_RESPONSE = """
                {
                  "type": "https://example.com/errors/duplicate-email",
                  "title": "Email already exists",
                  "status": 409,
                  "detail": "The email address provided is already registered.",
                  "instance": "/api/v1/auth/register"
            
                }
            """;


    public static final String USER_NOT_FOUND_RESPONSE = """
            {
              "type": "https://example.com/errors/authentication",
              "title": "User not found",
              "status": 404,
              "detail": "User not found with username: non-existent-user@example.com",
              "instance": "/api/v1/users/999"
            }
            """;


}
