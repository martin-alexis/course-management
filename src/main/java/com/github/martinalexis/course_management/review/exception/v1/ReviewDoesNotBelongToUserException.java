package com.github.martinalexis.course_management.review.exception.v1;

public class ReviewDoesNotBelongToUserException extends RuntimeException {
    public ReviewDoesNotBelongToUserException() {
        super("The review does not belong to the current user.");
    }
}
