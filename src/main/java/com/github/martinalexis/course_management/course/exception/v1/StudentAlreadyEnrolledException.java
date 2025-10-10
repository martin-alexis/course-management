package com.github.martinalexis.course_management.course.exception.v1;

public class StudentAlreadyEnrolledException extends RuntimeException {

    public StudentAlreadyEnrolledException() {
        super("The student has already enrolled in the course.");
    }

}
