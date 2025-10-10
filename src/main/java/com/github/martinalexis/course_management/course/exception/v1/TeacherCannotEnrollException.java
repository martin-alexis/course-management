package com.github.martinalexis.course_management.course.exception.v1;

public class TeacherCannotEnrollException extends RuntimeException {

    public TeacherCannotEnrollException() {
        super("The teacher cannot enroll in their own course");
    }

}
