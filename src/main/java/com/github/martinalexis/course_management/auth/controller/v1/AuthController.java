package com.github.martinalexis.course_management.auth.controller.v1;

import com.github.martinalexis.course_management.auth.dto.v1.AuthRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.AuthResponseDto;
import com.github.martinalexis.course_management.auth.dto.v1.RefreshTokenRequestDto;
import com.github.martinalexis.course_management.auth.dto.v1.RegisterRequestDto;
import com.github.martinalexis.course_management.auth.exceptions.v1.AuthExceptionJsonExamples;
import com.github.martinalexis.course_management.auth.service.v1.facade.AuthUseCase;
import com.github.martinalexis.course_management.common.exceptions.GlobalExceptionJsonExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication",
        description = """
        Endpoints for user authentication.

        ### Authentication Flows:

        1.  **Email/Password:**
            - Use the `POST /login` endpoint with email and password.

        2.  **Google OAuth2 (Social Login):**
            - This flow must be initiated from a browser.
            - **Step 1:** The client application must redirect the user to `GET /oauth2/authorization/google`.
            - **Step 2 (Final Implementation):** After successful authentication with Google, the user will be redirected to a frontend URL (e.g., `https://your-frontend.com/login/oauth2/code`) with `accessToken` and `refreshToken` as URL parameters.
            - **Step 3:** The frontend should capture these tokens from the URL and store them securely.

            **Note on Current Implementation:** As the frontend is not yet implemented, the backend currently does **not** perform the final redirect. Instead, after a successful Google login, it directly returns a JSON response containing the `accessToken` and `refreshToken`. This is a temporary measure for development and testing purposes.
        """
)@RequiredArgsConstructor
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
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data. One or more fields are invalid.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Validation Error",
                                    summary = "Example of a validation error",
                                    // Usando el ejemplo global para errores de validación
                                    value = GlobalExceptionJsonExamples.VALIDATION_FAILED_RESPONSE
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A unique constraint was violated (e.g., email already exists).",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Unique Constraint Violation",
                                    summary = "Example for a unique constraint violation",
                                    // Usando el ejemplo global para violación de unicidad (más preciso)
                                    value = GlobalExceptionJsonExamples.UNIQUE_CONSTRAINT_VIOLATION_RESPONSE
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Internal Error",
                                    summary = "Example for an internal server error",
                                    value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE
                            )
                    )
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
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data. One or more fields are invalid.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Validation Error",
                                    summary = "Example for a validation error",
                                    value = GlobalExceptionJsonExamples.VALIDATION_FAILED_RESPONSE
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials. The email or password provided is incorrect.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Invalid Credentials",
                                    summary = "Example for invalid credentials",
                                    value = AuthExceptionJsonExamples.BAD_CREDENTIALS_RESPONSE
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Internal Error",
                                    summary = "Example for an internal server error",
                                    value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE
                            )
                    )
            )
    })
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = authUseCase.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token. The refresh token itself is not changed.",
            security = {}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully. A new access token is provided.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data. The refresh token is missing.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Validation Error",
                                    summary = "Example for a missing refresh token",
                                    value = GlobalExceptionJsonExamples.VALIDATION_FAILED_RESPONSE
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Refresh token is expired, invalid, or malformed. The user must log in again.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Refresh Token Expired",
                                    summary = "Example for an expired refresh token",
                                    value = AuthExceptionJsonExamples.REFRESH_TOKEN_EXPIRED_RESPONSE
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error.",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Internal Error",
                                    summary = "Example for an internal server error",
                                    value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE
                            )
                    )
            )
    })
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto request) {
        AuthResponseDto response = authUseCase.refreshToken(request);
        return ResponseEntity.ok(response);
    }
} 