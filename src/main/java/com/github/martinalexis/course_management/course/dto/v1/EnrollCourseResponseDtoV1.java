package com.github.martinalexis.course_management.course.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EnrollCourseResponseDtoV1 {
    private Integer idCourses;
    private String title;
    private String description;
    private String studentName;
    private LocalDateTime inscriptionDate;
   }
