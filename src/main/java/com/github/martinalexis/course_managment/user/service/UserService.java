package com.github.martinalexis.course_managment.user.service;

import com.github.martinalexis.course_managment.user.dto.UserMapper;
import com.github.martinalexis.course_managment.user.dto.UserResponseDto;
import com.github.martinalexis.course_managment.user.model.UserModel;
import com.github.martinalexis.course_managment.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private  final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDto getUserById(int idUser) {
        UserModel user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

}
