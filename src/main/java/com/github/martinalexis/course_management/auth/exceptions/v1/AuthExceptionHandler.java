package com.github.martinalexis.course_management.auth.exceptions.v1;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
@Order(1)
@RequiredArgsConstructor
@RestControllerAdvice
public class AuthExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Invalid credentials");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/authentication"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ProblemDetail handleTokenExpired(TokenExpiredException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Token expired");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/authentication"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }


    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ProblemDetail handleRefreshTokenExpired(RefreshTokenExpiredException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Refresh token expired");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/refresh-token-expired"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }


}
