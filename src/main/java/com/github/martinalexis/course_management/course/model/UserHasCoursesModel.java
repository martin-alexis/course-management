package com.github.martinalexis.course_management.course.model;


import com.github.martinalexis.course_management.user.model.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_has_courses", uniqueConstraints = @UniqueConstraint(columnNames = {"users_id", "courses_id"}))
public class UserHasCoursesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users_has_courses")
    private Integer idUserHasCourses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private UserModel usersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courses_id", nullable = false)
    private CourseModel coursesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id", nullable = false)
    private RoleModel rolesId;

    @Column(name = "inscription_date", nullable = false)
    private LocalDateTime inscriptionDate;
}