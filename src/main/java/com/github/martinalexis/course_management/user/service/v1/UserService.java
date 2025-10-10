package com.github.martinalexis.course_management.user.service.v1;

import com.github.martinalexis.course_management.user.exception.v1.DuplicateEmailException;
import com.github.martinalexis.course_management.user.mapper.v1.UserMapper;
import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Domain service for user lifecycle operations.
 *
 * <p>Provides retrieval, registration, and OAuth2 upsert behavior. Passwords are
 * hashed using the configured {@link PasswordEncoder}.</p>
 *
 * @since 1.0
 */
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapperV1;

    /**
     * Retrieves a user by id or throws if not found.
     *
     * @param idUser unique user identifier
     * @return the persisted user entity
     * @throws UsernameNotFoundException if the user does not exist
     */
    public UserModel getUserById(Long idUser) {
        return userRepository.findById(idUser)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    /**
     * Registers a new local user after checking email uniqueness and hashing the password.
     *
     * @param newUser transient user carrying registration data
     * @return the persisted user entity
     * @throws DuplicateEmailException if the email is already registered
     */
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


    /**
     * Finds a user by email or throws if not found.
     *
     * @param email user email
     * @return the persisted user entity
     * @throws UsernameNotFoundException if no user is associated with the email
     */
    public UserModel findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Upserts a user based on OAuth2 identity data.
     *
     * <p>If the email exists, updates mutable fields (name, lastname) only when values
     * differ to avoid unnecessary writes. Otherwise, creates a new user without a password,
     * as credentials are managed by the external provider.</p>
     *
     * @param email OAuth2-provided unique email
     * @param name first name from the provider
     * @param lastname last name from the provider
     * @return the existing updated user or the newly created OAuth2 user
     */
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



