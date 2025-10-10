package com.github.martinalexis.course_management.course.service.v1;

import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.RoleEnum;
import com.github.martinalexis.course_management.course.model.RoleModel;
import com.github.martinalexis.course_management.course.model.UserHasCoursesModel;
import com.github.martinalexis.course_management.course.repository.UserHasCoursesRepository;
import com.github.martinalexis.course_management.user.model.UserModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserHasCoursesService {

    private final UserHasCoursesRepository userHasCoursesRepository;
    private final RoleService roleService;


    @Transactional
    public UserHasCoursesModel assignTeacherToCourse(UserModel user, CourseModel savedCourse) {

        RoleModel role = roleService.getRoleOrThrow(RoleEnum.TEACHER);

        UserHasCoursesModel assignTeacher = new UserHasCoursesModel();
        assignTeacher.setUser(user);
        assignTeacher.setCourse(savedCourse);
        assignTeacher.setRole(role);
        assignTeacher.setInscriptionDate(savedCourse.getCreatedOn());

        return userHasCoursesRepository.save(assignTeacher);
    }

    @Transactional
    public UserHasCoursesModel enrollStudent(UserModel user, CourseModel course) {

        RoleModel role = roleService.getRoleOrThrow(RoleEnum.STUDENT);

        UserHasCoursesModel enrollment = new UserHasCoursesModel();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setRole(role);
        enrollment.setInscriptionDate(LocalDateTime.now());

        return userHasCoursesRepository.save(enrollment);
    }



}
