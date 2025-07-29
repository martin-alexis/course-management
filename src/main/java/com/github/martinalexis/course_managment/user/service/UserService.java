package com.github.martinalexis.course_managment.user.service;

import com.github.martinalexis.course_managment.user.model.UserModel;
import com.github.martinalexis.course_managment.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel getUserById(int idUser) {
        return userRepository.findById(idUser).orElseThrow();
    }
}
