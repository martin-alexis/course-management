package com.github.martinalexis.course_management.course.exception.v1;

public class UserNotOwnCourseException extends RuntimeException {

    public UserNotOwnCourseException() {
        super("The user is not the owner of the course");
    }

}
