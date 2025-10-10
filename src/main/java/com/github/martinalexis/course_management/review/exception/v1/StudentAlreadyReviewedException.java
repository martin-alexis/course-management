package com.github.martinalexis.course_management.review.exception.v1;

public class StudentAlreadyReviewedException extends RuntimeException {

    public StudentAlreadyReviewedException() {
        super("The student has already rewiewed this course.");
    }

}
