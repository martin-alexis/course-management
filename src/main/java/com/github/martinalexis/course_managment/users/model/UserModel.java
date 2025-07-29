package com.github.martinalexis.course_managment.users.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    @Id
    @Column(name = "id_users")
    private Integer idUser;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;
}
