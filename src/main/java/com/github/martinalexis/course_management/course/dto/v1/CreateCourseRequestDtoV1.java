package com.github.martinalexis.course_management.course.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateCourseRequestDtoV1 {
    private String title;
    private String description;
   }
