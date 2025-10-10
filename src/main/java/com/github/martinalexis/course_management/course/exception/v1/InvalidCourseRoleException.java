package com.github.martinalexis.course_management.course.exception.v1;

import com.github.martinalexis.course_management.course.model.RoleEnum;

public class InvalidCourseRoleException extends RuntimeException {

    public InvalidCourseRoleException(RoleEnum expectedRole) {
        super(buildMessage(expectedRole));
    }

    private static String buildMessage(RoleEnum expectedRole) {
        if (expectedRole == RoleEnum.TEACHER) {
            return "User is not the owner of this course.";
        } else if (expectedRole == RoleEnum.STUDENT) {
            return "User is not enrolled as a student in this course.";
        }
        return "User does not have the required role for this course.";
    }

}
