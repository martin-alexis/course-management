package com.github.martinalexis.course_managment.auth.exceptions.v1;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("The email '" + email + "' is already registered");
    }
}
