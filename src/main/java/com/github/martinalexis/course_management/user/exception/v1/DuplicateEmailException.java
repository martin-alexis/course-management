package com.github.martinalexis.course_management.user.exception.v1;

/**
 * Exception thrown when attempting to register a user with an email that already exists.
 *
 * <p>Raised by the User module to signal a uniqueness constraint violation at the
 * application layer, before persisting the entity.</p>
 *
 * @since 1.0
 */
public class DuplicateEmailException extends RuntimeException {
    /**
     * Creates a new exception including the conflicting email in the message.
     *
     * @param email conflicting email address
     */
    public DuplicateEmailException(String email) {
        super("The email '" + email + "' is already registered");
    }
}
