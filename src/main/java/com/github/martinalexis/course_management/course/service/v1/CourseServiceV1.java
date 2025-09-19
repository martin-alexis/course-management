package com.github.martinalexis.course_management.course.service.v1;

import com.github.martinalexis.course_management.common.exceptions.ResourceNotFoundException;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseRequestDtoV1;
import com.github.martinalexis.course_management.course.dto.v1.CreateCourseResponseDtoV1;
import com.github.martinalexis.course_management.course.mapper.v1.CourseMapperV1;
import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.course.model.RoleEnum;
import com.github.martinalexis.course_management.course.model.RoleModel;
import com.github.martinalexis.course_management.course.model.UserHasCoursesModel;
import com.github.martinalexis.course_management.course.repository.CourseRepository;
import com.github.martinalexis.course_management.course.repository.RoleRepository;
import com.github.martinalexis.course_management.course.repository.UserHasCoursesRepository;
import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CourseServiceV1 {

    private final CourseRepository courseRepository;
    private final UserHasCoursesRepository userHasCourseRepository;
    private final RoleRepository roleRepository;
    private final CourseMapperV1 courseMapper;
    private final UserRepository userRepository;

    @Transactional
    public CreateCourseResponseDtoV1 createCourse(CreateCourseRequestDtoV1 request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());

        UserModel user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        CourseModel newCourse = courseMapper.toEntity(request);

        newCourse.setCreatedOn(LocalDateTime.now());

        CourseModel savedCourse = courseRepository.save(newCourse);

        RoleModel teacherRole = roleRepository.findByRole(RoleEnum.TEACHER)
                .orElseThrow(() -> new ResourceNotFoundException("role", RoleEnum.TEACHER));

        UserHasCoursesModel userHasCourse = new UserHasCoursesModel();
        userHasCourse.setUsersId(user);
        userHasCourse.setCoursesId(savedCourse);
        userHasCourse.setRolesId(teacherRole);
        userHasCourse.setInscriptionDate(savedCourse.getCreatedOn());

        userHasCourseRepository.save(userHasCourse);

        CreateCourseResponseDtoV1 response = courseMapper.toResponse(savedCourse);
        response.setTeacherName(user.getName() + " " + user.getLastname());

        return response;
    }
}