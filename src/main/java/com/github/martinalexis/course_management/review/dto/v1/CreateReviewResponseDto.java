package com.github.martinalexis.course_management.review.dto.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateReviewResponseDto {
    @Schema(description = "The ID of the review.", example = "1")
    private Integer idReview;

    @Schema(description = "Optional comment for the review.", example = "This course was amazing, I learned a lot!")
    private String comment;

    @Schema(description = "The score given to the course, from 1 to 5.", example = "5")
    private Integer score;

    @Schema(description = "The timestamp when the review was created.", example = "2023-10-27T10:00:00")
    private LocalDateTime createdOn;

}