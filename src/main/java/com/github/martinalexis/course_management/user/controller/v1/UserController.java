package com.github.martinalexis.course_management.user.controller.v1;

import com.github.martinalexis.course_management.auth.exceptions.v1.AuthExceptionJsonExamples;
import com.github.martinalexis.course_management.common.exceptions.GlobalExceptionJsonExamples;
import com.github.martinalexis.course_management.user.dto.v1.UserResponseDto;
import com.github.martinalexis.course_management.user.service.v1.facade.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes read-only endpoints for user information.
 *
 * <p>This controller acts as the HTTP boundary of the User module, delegating
 * business logic to the {@link UserUseCase} facade. Authentication is expected
 * to be handled by the security configuration (JWT/OAuth2), and exceptions are
 * translated into RFC-7807 responses by dedicated exception handlers.</p>
 *
 * <p>Base path: <code>/api/v1/users</code></p>
 *
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints for user information")
public class UserController {

    private final UserUseCase userUseCase;


    /**
     * Retrieves a user by its unique identifier.
     *
     * <p>Delegates to {@link UserUseCase#getById(Long)}. If the user does not exist, the
     * request will result in a 404 ProblemDetail produced by the module's exception handlers.</p>
     *
     * @param id unique user identifier
     * @return 200 OK with the user representation
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by id",
            description = "Returns a user by its identifier. Requires a valid JWT Bearer token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. A valid JWT Bearer token is required.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Unauthorized", value = AuthExceptionJsonExamples.UNAUTHORIZED_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the provided ID.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Resource Not Found", value = GlobalExceptionJsonExamples.RESOURCE_NOT_FOUND_RESPONSE))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(name = "Internal Error", value = GlobalExceptionJsonExamples.UNEXPECTED_ERROR_RESPONSE))
            )
    })
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        UserResponseDto user = userUseCase.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}