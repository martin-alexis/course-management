package com.github.martinalexis.course_management.course.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateCourseResponseDto {
    @Schema(description = "Course identifier", example = "101")
    private Long idCourses;
    @Schema(description = "Course title", example = "Introduction to Java")
    private String title;
    @Schema(description = "Course description", example = "Learn Java basics and OOP concepts")
    private String description;
    @Schema(description = "Teacher full name", example = "Alexis Martin")
    private String teacherName;
    @Schema(description = "Average rating", example = "4.5")
    private Double rating;
    @Schema(description = "Creation date", example = "2025-09-10T10:15:30")
    private LocalDateTime createdOn;
}
