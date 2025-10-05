package com.github.martinalexis.course_management.course.service.v1.facade.impl;

import com.github.martinalexis.course_management.auth.service.v1.AuthService;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDto;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDto;
import com.github.martinalexis.course_management.course.dto.v1.EnrollCourseResponseDto;
import com.github.martinalexis.course_management.course.dto.v1.UpdateCourseRequestDto;
import com.github.martinalexis.course_management.course.mapper.v1.CourseMapper;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.UserHasCoursesModel;
import com.github.martinalexis.course_management.course.service.v1.CourseServiceV1;
import com.github.martinalexis.course_management.course.service.v1.UserHasCoursesService;
import com.github.martinalexis.course_management.course.service.v1.facade.CourseUseCase;
import com.github.martinalexis.course_management.course.service.v1.rules.StudentAlreadyEnrolledRule;
import com.github.martinalexis.course_management.course.service.v1.rules.TeacherCannotEnrollInOwnCourseRule;
import com.github.martinalexis.course_management.course.service.v1.rules.VerifyUserOwnCourseRule;
import com.github.martinalexis.course_management.review.service.v1.ReviewService;
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
    private final ReviewService reviewService;
    private final StudentAlreadyEnrolledRule studentAlreadyEnrolledRule;
    private final TeacherCannotEnrollInOwnCourseRule teacherCannotEnrollInOwnCourseRule;
    private final VerifyUserOwnCourseRule verifyUserOwnCourseRule;
    private final CourseMapper courseMapper;

    @Override
    public CreateCourseResponseDto getById(int idCourse) {
        CourseModel course = courseService.findByIdOrThrow(idCourse);
        CreateCourseResponseDto response = courseMapper.toResponse(course);
        response.setTeacherName(courseService.getTeacherName(course));
        response.setRating(reviewService.calculateAverageRating(course.getReviews().stream().toList()));
        return response;
    }

    @Override
    public CreateCourseResponseDto updateCourse(int idCourse, UpdateCourseRequestDto request) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel courseToUpdate = courseService.findByIdOrThrow(idCourse);

        verifyUserOwnCourseRule.execute(currentUser, courseToUpdate);
        CourseModel course = courseMapper.updateCourseRequestToEntity(request);
        CourseModel updatedCourse = courseService.updateCourse(course, courseToUpdate);
        CreateCourseResponseDto response = courseMapper.toResponse(updatedCourse);
        response.setTeacherName(courseService.getTeacherName(updatedCourse));
        response.setRating(reviewService.calculateAverageRating(course.getReviews().stream().toList()));
        return response;


    }

    @Override
    public void deleteCourse(int idCourse) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel courseToDelete = courseService.findByIdOrThrow(idCourse);

        verifyUserOwnCourseRule.execute(currentUser, courseToDelete);

        courseService.deleteCourse(courseToDelete);
    }

    @Transactional
    @Override
    public CreateCourseResponseDto createCourse(CreateCourseRequestDto request) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel newCourse = courseMapper.toEntity(request);

        CourseModel savedCourse = courseService.saveCourse(newCourse);

        userHasCoursesService.assignTeacherToCourse(currentUser, savedCourse);

        CreateCourseResponseDto response = courseMapper.toResponse(savedCourse);
        response.setTeacherName(currentUser.getName() + " " + currentUser.getLastname());
        response.setRating(reviewService.calculateAverageRating(savedCourse.getReviews().stream().toList()));

        return response;
    }


    @Override
    public Page<CreateCourseResponseDto> getAllCourses(String search, Pageable pageable) {
        Page<CourseModel> courses;

        if (search == null || search.isBlank()) {
            courses = courseService.findAll(pageable);
        } else {
            courses = courseService.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    search, search, pageable
            );
        }

        return courses.map(course -> {
            CreateCourseResponseDto response = courseMapper.toResponse(course);
            response.setTeacherName(courseService.getTeacherName(course));
            response.setRating(reviewService.calculateAverageRating(course.getReviews().stream().toList()));
            return response;
        });
    }

    @Override
    public Page<CreateCourseResponseDto> getTeacherCourses(String search, Pageable pageable) {

        UserModel currentUser = authService.getCurrentUser();

        Page<CourseModel> courses = courseService.findTeacherCoursesWithSearch(currentUser.getIdUser(), search, pageable);


        return courses.map(course -> {
            CreateCourseResponseDto response = courseMapper.toResponse(course);
            response.setTeacherName(courseService.getTeacherName(course));
            response.setRating(reviewService.calculateAverageRating(course.getReviews().stream().toList()));
            return response;
        });
    }

    public Page<CreateCourseResponseDto> getStudentCourses(String search, Pageable pageable) {

        UserModel currentUser = authService.getCurrentUser();

        Page<CourseModel> courses = courseService.findStudentCoursesWithSearch(currentUser.getIdUser(), search, pageable);


        return courses.map(course -> {
            CreateCourseResponseDto response = courseMapper.toResponse(course);
            response.setTeacherName(courseService.getTeacherName(course));
            response.setRating(reviewService.calculateAverageRating(course.getReviews().stream().toList()));
            return response;
        });
    }

    @Transactional
    @Override
    public EnrollCourseResponseDto enrollStudentToCourse(int idCourse) {
        UserModel currentUser = authService.getCurrentUser();

        CourseModel course = courseService.findByIdOrThrow(idCourse);

        studentAlreadyEnrolledRule.execute(currentUser, course);

        teacherCannotEnrollInOwnCourseRule.execute(currentUser, course);

        UserHasCoursesModel enrollment = userHasCoursesService.enrollStudent(currentUser, course);

        EnrollCourseResponseDto response = courseMapper.toEnrollResponse(course);
        response.setStudentName(currentUser.getName() + " " + currentUser.getLastname());
        response.setInscriptionDate(enrollment.getInscriptionDate());

        return response;
    }
}
