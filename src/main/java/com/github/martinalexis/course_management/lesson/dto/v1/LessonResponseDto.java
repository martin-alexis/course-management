package com.github.martinalexis.course_management.lesson.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class LessonResponseDto {

    @Schema(description = "Unique identifier of the lesson", example = "1")
    private Long idLesson;

    @Schema(description = "Title of the lesson", example = "Introduction to Java")
    private String title;

    @Schema(description = "Description of the lesson", example = "Learn Java basics and OOP concepts")
    private String description;

    @Schema(description = "URL of the lesson video", example = "https://www.youtube.com/watch?v=someVideoId")
    private String videoUrl;

    @Schema(description = "Publication date of the lesson", example = "2025-09-10T10:15:30", nullable = true)
    private LocalDateTime publishedOn;
}
