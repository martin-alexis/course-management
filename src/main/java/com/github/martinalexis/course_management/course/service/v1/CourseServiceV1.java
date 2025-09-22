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
import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseServiceV1 {

    private final CourseRepository courseRepository;


    public CourseModel saveCourse(CourseModel course) {
        course.setCreatedOn(LocalDateTime.now());
        return courseRepository.save(course);
    }

    public CourseModel findByIdOrThrow(int idCourse) {
        return courseRepository.findById(idCourse)
                .orElseThrow(() -> new ResourceNotFoundException("course", idCourse));
    }

    public Page<CourseModel> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);

    }


    public String getTeacherName(CourseModel course) {
        return course.getUserCourses().stream()
                .filter(uc -> uc.getRolesId().getRole().equals(RoleEnum.TEACHER))
                .map(uc -> uc.getUsersId().getName() + " " + uc.getUsersId().getLastname())
                .findFirst()
                .orElse("Unknown");
    }

}