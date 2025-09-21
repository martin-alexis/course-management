package com.github.martinalexis.course_management.auth.controller.v1;

import com.github.martinalexis.course_management.auth.dto.v1.AuthRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.AuthResponseDto;
import com.github.martinalexis.course_management.auth.dto.v1.RefreshTokenRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.RegisterRequestDto;
import com.github.martinalexis.course_management.auth.service.v1.facade.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Authentication", description = "Endpoints for user registration, login and token refresh")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthUseCase authUseCase;

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
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns access and refresh tokens.",
            security = {}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed or duplicate email",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authUseCase.register(request);
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
    @Operation(
            summary = "Login with email and password",
            description = "Authenticates a user and returns access and refresh tokens.",
            security = {}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authenticated successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = authUseCase.login(request);
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
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token.",
            security = {}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Refresh token expired or invalid",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto request) {
        AuthResponseDto response = authUseCase.refreshToken(request);
        return ResponseEntity.ok(response);
    }
} 