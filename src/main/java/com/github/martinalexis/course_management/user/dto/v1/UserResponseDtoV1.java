package com.github.martinalexis.course_management.user.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserResponseDtoV1 {

    private Integer idUser;
    private String name;
    private String lastname;
    private String email;

}
