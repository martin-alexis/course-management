package com.github.martinalexis.course_management.course.service.v1.rules;

import com.github.martinalexis.course_management.course.exception.v1.TeacherCannotEnrollException;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.RoleEnum;
import com.github.martinalexis.course_management.course.repository.UserHasCoursesRepository;
import com.github.martinalexis.course_management.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeacherCannotEnrollInOwnCourseRule {
    private final UserHasCoursesRepository userHasCoursesRepository;

    public void execute(UserModel user, CourseModel course) {

        userHasCoursesRepository.findByUserAndCourse(user, course)
                .filter(uhc -> uhc.getRole().getRole() == RoleEnum.TEACHER)
                .ifPresent(uhc -> {
                    throw new TeacherCannotEnrollException();
                });
    }
}
