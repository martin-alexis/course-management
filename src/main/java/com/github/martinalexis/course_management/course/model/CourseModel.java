package com.github.martinalexis.course_management.course.model;
import com.github.martinalexis.course_management.lesson.model.LessonModel;
import com.github.martinalexis.course_management.review.model.ReviewModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class CourseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_courses")
    private Integer idCourses;

    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "coursesId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserHasCoursesModel> userCourses;

    @OneToMany(mappedBy = "coursesId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LessonModel> lessons;

    @OneToMany(mappedBy = "coursesId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewModel> reviews;
}

