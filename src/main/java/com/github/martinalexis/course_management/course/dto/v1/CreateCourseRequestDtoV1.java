package com.github.martinalexis.course_management.course.dto.v1;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateCourseRequestDtoV1 {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 45, message = "Title must be at most 45 characters")
    private String title;
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;
   }
