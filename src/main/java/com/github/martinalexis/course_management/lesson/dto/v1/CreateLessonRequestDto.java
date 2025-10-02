package com.github.martinalexis.course_management.lesson.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateLessonRequestDto {

    @NotBlank(message = "Title is mandatory")
    @Schema(description = "Title of the lesson", example = "Introduction to Java", required = true)
    private String title;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 255, message = "Description must be at most 255 characters")
    @Schema(description = "Description of the lesson", example = "Learn Java basics and OOP concepts", required = true)
    private String description;

    @NotBlank(message = "url video is mandatory")
    @URL(message = "Video URL must be a valid URL")
    @Schema(description = "URL of the lesson video", example = "https://www.youtube.com/watch?v=someVideoId", required = true)
    private String videoUrl;

}
