package com.github.martinalexis.course_management.course.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public class UpdateCourseRequestDto {
        @Schema(description = "Course title", example = "Introduction to Java")
        @Size(max = 45, message = "Title must be at most 45 characters")
        private String title;

        @Schema(description = "Course description", example = "Learn Java basics and OOP concepts")
        @Size(max = 255, message = "Description must be at most 255 characters")
        private String description;
    }


