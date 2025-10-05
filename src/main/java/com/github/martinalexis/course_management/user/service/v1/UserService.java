package com.github.martinalexis.course_management.user.service.v1;

import com.github.martinalexis.course_management.user.exception.v1.DuplicateEmailException;
import com.github.martinalexis.course_management.user.mapper.v1.UserMapper;
import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapperV1;

    public UserModel getUserById(Long idUser) {
        return userRepository.findById(idUser)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public UserModel registerNewUser(UserModel newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicateEmailException(newUser.getEmail());
        }

        UserModel user = UserModel.builder()
                .name(newUser.getName())
                .lastname(newUser.getLastname())
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .build();

        return userRepository.save(user);
    }


    public UserModel findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserModel findOrCreateOAuth2User(String email, String name, String lastname) {
        return userRepository.findByEmail(email)
                .map(user -> OAuthUpdateIfNeeded(user, name, lastname))
                .orElseGet(() -> createOAuth2User(email, name, lastname));
    }

    private UserModel OAuthUpdateIfNeeded(UserModel user, String name, String lastname) {
        boolean updated = false;

        if (!user.getName().equals(name)) {
            user.setName(name);
            updated = true;
        }
        if (!user.getLastname().equals(lastname)) {
            user.setLastname(lastname);
            updated = true;
        }
        return updated ? userRepository.save(user) : user;
    }

    private UserModel createOAuth2User(String email, String name, String lastname) {
        UserModel user = UserModel.builder()
                .email(email)
                .name(name)
                .lastname(lastname)
                .password(null) // OAuth2 users don’t need password
                .build();
        return userRepository.save(user);
    }
}



