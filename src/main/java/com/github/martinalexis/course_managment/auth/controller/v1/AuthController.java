package com.github.martinalexis.course_managment.auth.controller.v1;

import com.github.martinalexis.course_managment.auth.dto.v1.AuthRequestDto;
import com.github.martinalexis.course_managment.auth.dto.v1.AuthResponseDto;
import com.github.martinalexis.course_managment.auth.dto.v1.RefreshTokenRequestDto;
import com.github.martinalexis.course_managment.auth.dto.v1.RegisterRequestDto;
import com.github.martinalexis.course_managment.auth.service.v1.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 *
 * This controller provides endpoints for:
 * - User registration with JWT token generation
 * - Local user authentication (email/password)
 * - JWT token refresh functionality
 * - OAuth2 authentication (handled by OAuth2AuthenticationSuccessHandler)
 *
 * All endpoints return JWT tokens for subsequent API access.
 * The controller integrates with Spring Security for authentication
 * and validation of incoming requests.
 *
 * @author Alexis Martin
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user and returns JWT authentication tokens.
     *
     * This endpoint:
     * 1. Validates the registration request data
     * 2. Checks for existing email addresses
     * 3. Creates a new user with encrypted password
     * 4. Generates access and refresh JWT tokens
     * 5. Returns authentication response with user information
     *
     * **Request Validation:**
     * - Email must be unique and valid format
     * - Name and lastname are required
     * - Password is required and will be encrypted
     *
     * **Response:**
     * - accessToken: JWT for API access (24 hours)
     * - refreshToken: JWT for token renewal (7 days)
     * - user: complete user information
     * - expiresIn: token expiration time
     * 
     * @param request the registration request containing user details
     * @return ResponseEntity with authentication response and JWT tokens
     * @throws DuplicateEmailException if email is already registered
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user with email and password, returning JWT tokens.
     * 
     * This endpoint:
     * 1. Validates user credentials using Spring Security
     * 2. Generates new access and refresh JWT tokens
     * 3. Returns authentication response with user information
     * 
     * **Authentication Process:**
     * - Uses Spring Security's AuthenticationManager
     * - Validates email/password combination
     * - Loads user details for token generation
     * - Creates new JWT tokens for the session
     * 
     * **Security Features:**
     * - Password comparison using BCrypt
     * - Protection against brute force attacks
     * - Secure token generation
     * 
     * @param request the authentication request containing credentials
     * @return ResponseEntity with authentication response and JWT tokens
     * @throws UsernameNotFoundException if user credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = authService.authenticate(authenticationManager, request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    /**
     * Refreshes an expired access token using a valid refresh token.
     * 
     * This endpoint:
     * 1. Validates the provided refresh token
     * 2. Extracts user information from the token
     * 3. Generates a new access token
     * 4. Returns the new access token with the existing refresh token
     * 
     * **Token Refresh Process:**
     * - Validates refresh token signature and expiration
     * - Loads current user information
     * - Generates new access token with same user context
     * - Maintains refresh token for future renewals
     * 
     * **Use Cases:**
     * - Automatic token renewal in SPAs
     * - Maintaining user sessions
     * - Reducing authentication overhead
     * 
     * @param request the refresh token request containing the token to use for renewal
     * @return ResponseEntity with new access token and existing refresh token
     * @throws RefreshTokenExpiredException if refresh token is invalid or expired
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto request) {
        AuthResponseDto response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
} 