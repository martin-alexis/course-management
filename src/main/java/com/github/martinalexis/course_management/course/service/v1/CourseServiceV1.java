package com.github.martinalexis.course_management.course.service.v1;

import com.github.martinalexis.course_management.common.exceptions.ResourceNotFoundException;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.RoleEnum;
import com.github.martinalexis.course_management.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CourseServiceV1 {

    private final CourseRepository courseRepository;


    public CourseModel saveCourse(CourseModel course) {
        course.setCreatedOn(LocalDateTime.now());
        return courseRepository.save(course);
    }

    public CourseModel updateCourse(CourseModel course, CourseModel courseToUpdate) {

        if (course.getTitle() != null) {
            courseToUpdate.setTitle(course.getTitle());
        }
        if (course.getDescription() != null) {
            courseToUpdate.setDescription(course.getDescription());
        }

        return courseRepository.save(courseToUpdate);
    }

    public CourseModel deleteCourse(CourseModel course) {
        courseRepository.delete(course);
        return course;
    }

    public CourseModel findByIdOrThrow(int idCourse) {
        return courseRepository.findById(idCourse)
                .orElseThrow(() -> new ResourceNotFoundException("course", idCourse));
    }

    public Page<CourseModel> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);

    }

    public Page<CourseModel> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable) {
        return courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(title, description, pageable);
    }

    public Page<CourseModel> findTeacherCoursesWithSearch(int idUser, String search, Pageable pageable) {
        return courseRepository.findCoursesWithRoleAndSearch(idUser, RoleEnum.TEACHER, search, pageable);
    }

    public Page<CourseModel> findStudentCoursesWithSearch(int idUser, String search, Pageable pageable) {
        return courseRepository.findCoursesWithRoleAndSearch(idUser, RoleEnum.STUDENT, search, pageable);
    }

    public String getTeacherName(CourseModel course) {
        return course.getUserCourses().stream()
                .filter(uc -> uc.getRole().getRole().equals(RoleEnum.TEACHER))
                .map(uc -> uc.getUser().getName() + " " + uc.getUser().getLastname())
                .findFirst()
                .orElse("Unknown");
    }

}