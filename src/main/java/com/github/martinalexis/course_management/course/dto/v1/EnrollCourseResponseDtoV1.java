package com.github.martinalexis.course_management.course.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EnrollCourseResponseDtoV1 {
    @Schema(description = "Course identifier", example = "101")
    private Integer idCourses;
    @Schema(description = "Course title", example = "Introduction to Java")
    private String title;
    @Schema(description = "Course description", example = "Learn Java basics and OOP concepts")
    private String description;
    @Schema(description = "Enrolled student full name", example = "John Doe")
    private String studentName;
    @Schema(description = "Enrollment timestamp", example = "2025-09-10T11:00:00")
    private LocalDateTime inscriptionDate;
}
