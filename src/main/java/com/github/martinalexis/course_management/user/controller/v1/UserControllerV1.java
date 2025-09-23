package com.github.martinalexis.course_management.user.controller.v1;

import com.github.martinalexis.course_management.user.dto.v1.UserResponseDtoV1;
import com.github.martinalexis.course_management.user.service.v1.UserServiceV1;
import com.github.martinalexis.course_management.user.service.v1.facade.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints for user information")
public class UserControllerV1 {

    private final UserUseCase userUseCase;


    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by id",
            description = "Returns a user by its identifier. Requires JWT Bearer token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponseDtoV1.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected error",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<UserResponseDtoV1> getUserById(@PathVariable("id") Integer id) {
        UserResponseDtoV1 user = userUseCase.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}