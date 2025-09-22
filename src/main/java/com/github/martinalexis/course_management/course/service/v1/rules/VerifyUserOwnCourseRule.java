package com.github.martinalexis.course_management.course.service.v1.rules;

import com.github.martinalexis.course_management.course.exception.v1.UserNotOwnCourseException;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.RoleEnum;
import com.github.martinalexis.course_management.user.model.UserModel;
import org.springframework.stereotype.Service;

@Service
public class VerifyUserOwnCourseRule {

    public void execute(UserModel user, CourseModel course) {
        boolean isTeacher = course.getUserCourses().stream()
                .anyMatch(uc -> uc.getUsersId().getIdUser().equals(user.getIdUser()) && uc.getRolesId().getRole().equals(RoleEnum.TEACHER));

        if (!isTeacher) {
            throw new UserNotOwnCourseException();
        }
    }
}
