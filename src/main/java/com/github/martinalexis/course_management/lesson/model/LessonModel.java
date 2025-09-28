package com.github.martinalexis.course_management.lesson.model;

import com.github.martinalexis.course_management.course.model.CourseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessons", schema = "course_management")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LessonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lessons")
    @EqualsAndHashCode.Include
    private Integer idLesson;

    @Column(name = "title", nullable = false, unique = true, length = 45)
    private String title;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "video_url", nullable = false, columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courses_id", nullable = false)
    private CourseModel course;
}