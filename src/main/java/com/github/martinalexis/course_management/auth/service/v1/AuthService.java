package com.github.martinalexis.course_management.auth.service.v1;

import com.github.martinalexis.course_management.auth.dto.v1.AuthResponseDto;
import com.github.martinalexis.course_management.auth.dto.v1.RegisterRequestDto;
import com.github.martinalexis.course_management.user.exception.v1.DuplicateEmailException;
import com.github.martinalexis.course_management.auth.exceptions.v1.RefreshTokenExpiredException;

import com.github.martinalexis.course_management.user.model.UserModel;
import com.github.martinalexis.course_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Core authentication service that handles user authentication and authorization.
 * <p>
 * This service provides:
 * - User registration with password hashing
 * - Local authentication (email/password)
 * - OAuth2 user processing and token generation
 * - JWT token refresh functionality
 * - User management operations
 * <p>
 * The service integrates with Spring Security for authentication and uses
 * JWT tokens for stateless API access.
 *
 * @author Alexis Martin
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());

        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    public void authenticateCredentials(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}