package com.github.martinalexis.course_management.common.exceptions;

public class GlobalExceptionJsonExamples {

    /**
     * Example response for when input data validation fails (e.g., a required field is missing).
     * This results in a 400 Bad Request status.
     * Corresponds to: handleValidationException(MethodArgumentNotValidException ex)
     */
    public static final String VALIDATION_FAILED_RESPONSE = """
                {
                  "type": "https://example.com/errors/validation",
                  "title": "Validation failed",
                  "status": 400,
                  "instance": "/api/v1/auth/endpoint",
                  "invalid-params": [
                    {
                      "name": "name",
                      "reason": "reason"
                    },
                    {
                      "name": "name",
                      "reason": "reason"
                    }
                  ]
                }
            """;

    /**
     * Example response for when a specific resource (like a course, user, etc.)
     * cannot be found by its identifier.
     * This results in a 404 Not Found status.
     * Corresponds to: handleResourceNotFound(ResourceNotFoundException ex)
     */
    public static final String RESOURCE_NOT_FOUND_RESPONSE = """
                {
                  "type": "https://example.com/errors/resource-not-found",
                  "title": "Resource not found",
                  "status": 404,
                  "detail": "Resource of type 'recourse' with ID '999' not found.",
                  "instance": "/api/v1/recourse/999"
                }
            """;

    /**
     * Example response for when an operation violates a unique database constraint (e.g., duplicate email).
     * This results in a 409 Conflict status.
     * Corresponds to: handleUniqueConstraintViolation(DataIntegrityViolationException ex)
     */
    public static final String UNIQUE_CONSTRAINT_VIOLATION_RESPONSE = """
                {
                  "type": "https://example.com/errors/unique-constraint",
                  "title": "Unique constraint violation",
                  "status": 409,
                  "detail": "The value for 'value' already exists and must be unique.",
                  "instance": "/api/v1/auth/register"
                }
            """;

    /**
     * Example response for an unexpected, unhandled server-side error.
     * This results in a 500 Internal Server Error status.
     * Corresponds to: handleGenericException(Exception ex)
     */
    public static final String UNEXPECTED_ERROR_RESPONSE = """
                {
                  "type": "https://example.com/errors/unexpected",
                  "title": "Unexpected error",
                  "status": 500,
                  "detail": "An unexpected error occurred",
                  "instance": "/api/v1/some-endpoint"
                }
            """;
}

