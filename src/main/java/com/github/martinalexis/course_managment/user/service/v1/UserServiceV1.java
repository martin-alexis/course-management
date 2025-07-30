package com.github.martinalexis.course_managment.user.service.v1;

import com.github.martinalexis.course_managment.user.mapper.v1.UserMapperV1;
import com.github.martinalexis.course_managment.user.dto.v1.UserResponseDtoV1;
import com.github.martinalexis.course_managment.user.model.UserModel;
import com.github.martinalexis.course_managment.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceV1 {

    private final UserRepository userRepository;
    private  final UserMapperV1 userMapperV1;

    public UserServiceV1(UserRepository userRepository, UserMapperV1 userMapperV1) {
        this.userRepository = userRepository;
        this.userMapperV1 = userMapperV1;
    }

    public UserResponseDtoV1 getUserById(int idUser) {
        UserModel user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapperV1.toDto(user);
    }

}
