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
 *
 * This service provides:
 * - User registration with password hashing
 * - Local authentication (email/password)
 * - OAuth2 user processing and token generation
 * - JWT token refresh functionality
 * - User management operations
 *
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
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());

        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Registers a new user with encrypted password and generates JWT tokens.
     *
     * This method:
     * 1. Validates that the email is not already registered
     * 2. Encrypts the password using BCrypt
     * 3. Creates and persists the new user
     * 4. Generates access and refresh JWT tokens
     * 5. Returns authentication response with tokens and user data
     *
     * @param request the registration request containing user details
     * @return AuthResponseDto containing JWT tokens and user information
     * @throws DuplicateEmailException if email is already registered
     */
    public AuthResponseDto register(RegisterRequestDto request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException(request.getEmail());
        }

        // Create user with hashed password
        UserModel user = UserModel.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        // Generate tokens using userId as subject
        UserDetails userDetails = userDetailsService.loadUserById(user.getIdUser());
        String jwt = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }

    /**
     * Authenticates a user with email and password, returning JWT tokens.
     *
     * This method:
     * 1. Validates user credentials using Spring Security's AuthenticationManager
     * 2. Loads user details for token generation
     * 3. Generates new access and refresh JWT tokens
     * 4. Returns authentication response with tokens and user data
     *
     * @param authenticationManager Spring Security's authentication manager
     * @param email user's email address
     * @param password user's plain text password
     * @return AuthResponseDto containing JWT tokens and user information
     * @throws UsernameNotFoundException if user is not found
     */
    public AuthResponseDto authenticate(AuthenticationManager authenticationManager, String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserById(user.getIdUser());
        String jwt = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }

    /**
     * Processes OAuth2 user authentication and generates JWT tokens.
     *
     * This method handles users who authenticate through Google OAuth2:
     * 1. Checks if user already exists in the system
     * 2. Updates existing user information if needed
     * 3. Creates new user if they don't exist
     * 4. Generates JWT tokens for API access
     * 5. Returns authentication response with tokens and user data
     *
     * @param email user's email from Google OAuth2
     * @param name user's first name from Google OAuth2
     * @param lastname user's last name from Google OAuth2
     * @return AuthResponseDto containing JWT tokens and user information
     */
    public AuthResponseDto processOAuth2User(String email, String name, String lastname) {
        Optional<UserModel> existingUser = userRepository.findByEmail(email);

        UserModel user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            // Update existing user information if it changed
            boolean updated = false;
            if (!user.getName().equals(name)) {
                user.setName(name);
                updated = true;
            }
            if (!user.getLastname().equals(lastname)) {
                user.setLastname(lastname);
                updated = true;
            }
            if (updated) {
                userRepository.save(user);
            }
        } else {
            // Create new user for OAuth2 authentication
            user = UserModel.builder()
                    .email(email)
                    .name(name)
                    .lastname(lastname)
                    .password(null) // OAuth2 users don't have local passwords
                    .build();
            userRepository.save(user);
        }

        // Generate JWT tokens for the user
        UserDetails userDetails = userDetailsService.loadUserById(user.getIdUser());
        String jwt = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }

    /**
     * Refreshes an expired access token using a valid refresh token.
     * 
     * This method:
     * 1. Extracts user information from the refresh token
     * 2. Validates that the refresh token is not expired
     * 3. Generates a new access token
     * 4. Returns the new access token with the existing refresh token
     * 
     * @param refreshToken the refresh token to use for renewal
     * @return AuthResponseDto containing new access token and existing refresh token
     * @throws RefreshTokenExpiredException if refresh token is invalid or expired
     */
    public AuthResponseDto refreshToken(String refreshToken) {
        String idUser = jwtService.extractIdUser(refreshToken);

        if (idUser != null && !jwtService.isTokenExpired(refreshToken)) {
            Integer id = Integer.parseInt(idUser);
            UserDetails userDetails = userDetailsService.loadUserById(id);
            String newAccessToken = jwtService.generateToken(userDetails);

            return AuthResponseDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtService.getJwtExpiration())
                    .build();
        }

        throw new RefreshTokenExpiredException();
    }
} 