package com.github.martinalexis.course_management.user.service.v1;

import com.github.martinalexis.course_management.common.exceptions.ResourceNotFoundException;
import com.github.martinalexis.course_management.user.mapper.v1.UserMapperV1;
import com.github.martinalexis.course_management.user.dto.v1.UserResponseDtoV1;
import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class UserServiceV1 {

    private final UserRepository userRepository;
    private  final UserMapperV1 userMapperV1;

    public UserResponseDtoV1 getUserById(int idUser) {
        UserModel user = userRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("user", idUser));
        return userMapperV1.toDto(user);
    }

}
