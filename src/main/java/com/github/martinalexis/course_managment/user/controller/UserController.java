package com.github.martinalexis.course_managment.user.controller;

import com.github.martinalexis.course_managment.user.dto.v1.UserResponseDtoV1;
import com.github.martinalexis.course_managment.user.service.v1.UserServiceV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceV1 userServiceV1;

    public UserController(UserServiceV1 userServiceV1) {
        this.userServiceV1 = userServiceV1;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDtoV1> getUserById(@PathVariable("id") Integer id) {
        UserResponseDtoV1 user = userServiceV1.getUserById(id);
        return ResponseEntity.ok(user);
    }
}