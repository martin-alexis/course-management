package com.github.martinalexis.course_management.auth.service.v1.facade;

import com.github.martinalexis.course_management.auth.dto.v1.AuthRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.AuthResponseDto;
import com.github.martinalexis.course_management.auth.dto.v1.RefreshTokenRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.RegisterRequestDto;

/**
 * Interface defining authentication use cases for the facade pattern.
 * 
 * This interface defines the main authentication operations that can be performed:
 * - User registration with JWT token generation
 * - User authentication (login) with email/password
 * - OAuth2 user processing and token generation
 * - JWT token refresh functionality
 * 
 * The facade pattern allows for better separation of concerns and makes it easier
 * to add business rules and validation logic without affecting the controller layer.
 * 
 * @author Alexis Martin
 * @version 1.0
 */
public interface AuthUseCase {
    
    /**
     * Registers a new user and generates JWT authentication tokens.
     * 
     * @param request the registration request containing user details
     * @return AuthResponseDto containing JWT tokens and user information
     */
    AuthResponseDto register(RegisterRequestDto request);
    
    /**
     * Authenticates a user with email and password, returning JWT tokens.
     * 
     * @param request the authentication request containing email and password
     * @return AuthResponseDto containing JWT tokens and user information
     */
    AuthResponseDto login(AuthRequestDto request);
    
    /**
     * Processes OAuth2 user authentication and generates JWT tokens.
     * 
     * @param email user's email from OAuth2 provider
     * @param name user's first name from OAuth2 provider
     * @param lastname user's last name from OAuth2 provider
     * @return AuthResponseDto containing JWT tokens and user information
     */
    AuthResponseDto processOAuth2User(String email, String name, String lastname);
    
    /**
     * Refreshes an expired access token using a valid refresh token.
     * 
     * @param request the refresh token request containing the refresh token
     * @return AuthResponseDto containing new access token and existing refresh token
     */
    AuthResponseDto refreshToken(RefreshTokenRequestDto request);
}
