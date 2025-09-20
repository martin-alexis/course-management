package com.github.martinalexis.course_management.course.service.v1.facade.impl;

import com.github.martinalexis.course_management.auth.service.v1.AuthService;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.mapper.v1.CourseMapperV1;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.UserHasCoursesModel;
import com.github.martinalexis.course_management.course.service.v1.CourseServiceV1;
import com.github.martinalexis.course_management.course.service.v1.UserHasCoursesService;
import com.github.martinalexis.course_management.course.service.v1.facade.CourseUseCase;
import com.github.martinalexis.course_management.course.service.v1.rules.StudentAlreadyEnrolledRule;
import com.github.martinalexis.course_management.course.service.v1.rules.TeacherCannotEnrollInOwnCourseRule;
import com.github.martinalexis.course_management.user.model.UserModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CourseFacade implements CourseUseCase {
    private final AuthService authService;
    private final CourseServiceV1 courseService;
    private final UserHasCoursesService userHasCoursesService;
    private final StudentAlreadyEnrolledRule studentAlreadyEnrolledRule;
    private final TeacherCannotEnrollInOwnCourseRule teacherCannotEnrollInOwnCourseRule;
    private final CourseMapperV1 courseMapper;

    @Transactional
    @Override
    public CreateCourseResponseDtoV1 createCourse(CreateCourseRequestDtoV1 request) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel newCourse = courseMapper.toEntity(request);

        CourseModel savedCourse = courseService.saveCourse(newCourse);

        userHasCoursesService.assignTeacherToCourse(currentUser, savedCourse);

        CreateCourseResponseDtoV1 response = courseMapper.toResponse(savedCourse);
        response.setTeacherName(currentUser.getName() + " " + currentUser.getLastname());

        return response;
    }

    @Transactional
    @Override
    public EnrollCourseResponseDtoV1 enrollStudentToCourse(int idCourse) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel course = courseService.findByIdOrThrow(idCourse);

        studentAlreadyEnrolledRule.execute(currentUser, course);

        teacherCannotEnrollInOwnCourseRule.execute(currentUser, course);

        UserHasCoursesModel enrollment = userHasCoursesService.enrollStudent(currentUser, course);

        EnrollCourseResponseDtoV1 response = courseMapper.toEnrollResponse(course);
        response.setStudentName(currentUser.getName() + " " + currentUser.getLastname());
        response.setInscriptionDate(enrollment.getInscriptionDate());

        return response;
    }
}
