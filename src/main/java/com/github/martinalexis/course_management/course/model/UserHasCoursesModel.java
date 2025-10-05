package com.github.martinalexis.course_management.course.model;


import com.github.martinalexis.course_management.user.model.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_has_courses", uniqueConstraints = @UniqueConstraint(columnNames = {"users_id", "courses_id"}))
public class UserHasCoursesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users_has_courses")
    @EqualsAndHashCode.Include
    private Long idUserHasCourses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courses_id", nullable = false)
    private CourseModel course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id", nullable = false)
    private RoleModel role;

    @Column(name = "inscription_date", nullable = false)
    private LocalDateTime inscriptionDate;
}