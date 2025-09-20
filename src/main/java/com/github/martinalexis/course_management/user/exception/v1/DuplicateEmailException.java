package com.github.martinalexis.course_management.user.exception.v1;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("The email '" + email + "' is already registered");
    }
}
