package com.github.martinalexis.course_management.review.model;


import com.github.martinalexis.course_management.course.model.CourseModel;
import com.github.martinalexis.course_management.user.model.UserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reviews")
    @EqualsAndHashCode.Include
    private Integer idReview;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Min(1) @Max(5)
    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courses_id", nullable = false)
    private CourseModel course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private UserModel user;
}