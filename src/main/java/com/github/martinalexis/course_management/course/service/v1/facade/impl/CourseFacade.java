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
import com.github.martinalexis.course_management.course.service.v1.rules.VerifyUserOwnCourseRule;
import com.github.martinalexis.course_management.user.model.UserModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CourseFacade implements CourseUseCase {
    private final AuthService authService;
    private final CourseServiceV1 courseService;
    private final UserHasCoursesService userHasCoursesService;
    private final StudentAlreadyEnrolledRule studentAlreadyEnrolledRule;
    private final TeacherCannotEnrollInOwnCourseRule teacherCannotEnrollInOwnCourseRule;
    private final VerifyUserOwnCourseRule verifyUserOwnCourseRule;
    private final CourseMapperV1 courseMapper;

    @Override
    public CreateCourseResponseDtoV1 getById(int idCourse) {
        CourseModel course = courseService.findByIdOrThrow(idCourse);
        CreateCourseResponseDtoV1 response = courseMapper.toResponse(course);
        response.setTeacherName(courseService.getTeacherName(course));
        return response;
    }

    @Override
    public CreateCourseResponseDtoV1 updateCourse(int idCourse, CreateCourseRequestDtoV1 request) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel courseToUpdate = courseService.findByIdOrThrow(idCourse);

        verifyUserOwnCourseRule.execute(currentUser, courseToUpdate);
        CourseModel course = courseMapper.toEntity(request);
        CourseModel updatedCourse = courseService.updateCourse(course, courseToUpdate);
        CreateCourseResponseDtoV1 response = courseMapper.toResponse(updatedCourse);
        response.setTeacherName(courseService.getTeacherName(updatedCourse));
        return response;


    }

    @Override
    public CreateCourseResponseDtoV1 deleteCourse(int idCourse) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel courseToDelete = courseService.findByIdOrThrow(idCourse);

        verifyUserOwnCourseRule.execute(currentUser, courseToDelete);

        CourseModel deletedCourse = courseService.deleteCourse(courseToDelete);

        return courseMapper.toResponse(deletedCourse);
    }

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


    //    public Page<CreateCourseResponseDtoV1> getAllCourses(Pageable pageable) {
//        return courseService
//                .findAll(pageable)
//                .map(course -> {
//                    CreateCourseResponseDtoV1 response = courseMapper.toResponse(course);
//                    response.setTeacherName(courseService.getTeacherName(course));
//                    return response;
//                });
//    }
    @Override
    public Page<CreateCourseResponseDtoV1> getAllCourses(String search, Pageable pageable) {
        Page<CourseModel> courses;

        if (search == null || search.isBlank()) {
            courses = courseService.findAll(pageable);
        } else {
            courses = courseService.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    search, search, pageable
            );
        }

        return courses.map(course -> {
            CreateCourseResponseDtoV1 response = courseMapper.toResponse(course);
            response.setTeacherName(courseService.getTeacherName(course));
            return response;
        });
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
