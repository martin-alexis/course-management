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
/**
 * Centralized exception translation for the User module.
 *
 * <p>Converts domain/service exceptions into RFC-7807 {@link ProblemDetail}
 * to provide consistent error responses to API consumers.</p>
 *
 * @since 1.0
 */
@RequiredArgsConstructor
@Order(1)
@RestControllerAdvice
public class UserExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(UserExceptionHandler.class);

    /**
     * Handles missing users by translating {@link UsernameNotFoundException} into a
     * 404 ProblemDetail.
     *
     * @param ex thrown when a user cannot be found
     * @param request current web request, used to set the instance URI
     * @return 404 ProblemDetail describing the missing resource
     */
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

    /**
     * Handles attempts to register a duplicated email.
     *
     * <p>Returns a 400 ProblemDetail to communicate input validation issues at the API boundary.</p>
     *
     * @param ex raised when an email already exists in the system
     * @param request current web request, used to set the instance URI
     * @return 400 ProblemDetail describing the duplicate email violation
     */
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
