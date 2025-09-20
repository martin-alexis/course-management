package com.github.martinalexis.course_management.user.exception.v1;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
@RequiredArgsConstructor
@Order(1)
@RestControllerAdvice
public class UserExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(UserExceptionHandler.class);

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUserNotFound(UsernameNotFoundException ex, WebRequest request) {
        log.warn(ex.getMessage(), ex);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("User not found");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/authentication"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ProblemDetail handleDuplicateEmail(DuplicateEmailException ex, WebRequest request) {
        log.warn(ex.getMessage(), ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Duplicate email");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/duplicate-email"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }
}
