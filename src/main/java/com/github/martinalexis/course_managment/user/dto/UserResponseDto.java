package com.github.martinalexis.course_managment.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResponseDto {

    private Integer idUser;
    private String name;
    private String lastname;
    private String email;

}
