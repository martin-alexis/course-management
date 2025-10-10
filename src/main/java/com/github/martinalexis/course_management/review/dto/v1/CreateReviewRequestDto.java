package com.github.martinalexis.course_management.review.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateReviewRequestDto {

    @Schema(description = "Optional comment for the review.", example = "This course was amazing, I learned a lot!")
    private String comment;

    @NotNull(message = "Score is mandatory")
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    @Schema(description = "The score given to the course, from 1 to 5.", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer score;

}