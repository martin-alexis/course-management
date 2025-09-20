package com.github.martinalexis.course_management.common.exceptions;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleUniqueConstraintViolation(DataIntegrityViolationException ex, WebRequest request) {
        log.error("Unique constraint violation", ex);
        String detailMessage = "A unique constraint was violated.";

        Throwable cause = ex.getCause();
        if (cause instanceof ConstraintViolationException constraintEx) {
            String dbConstraintName = constraintEx.getConstraintName();
            if (dbConstraintName != null) {
                detailMessage = String.format("The value for '%s' already exists and must be unique.", dbConstraintName);
            }
        }

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT); // 409 Conflict
        problem.setTitle("Unique constraint violation");
        problem.setDetail(detailMessage);
        problem.setType(URI.create("https://example.com/errors/unique-constraint"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.warn("Resource not found: {}", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Resource not found");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/resource-not-found"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }


    // -------------------------
    // DTO's validations (@Valid)
    // -------------------------
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException ex,
                                                   WebRequest request) {
        log.warn("Validation failed: {}", ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation failed");
        problem.setType(URI.create("https://example.com/errors/validation"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        List<Map<String, String>> invalidParams = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> Map.of(
                        "name", err.getField(),
                        "reason", err.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        problem.setProperty("invalid-params", invalidParams);
        return problem;
    }

    // -------------------------
    // Unexpected errors
    // -------------------------
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected exception occurred", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Unexpected error");
        problem.setDetail("An unexpected error occurred");
        problem.setType(URI.create("https://example.com/errors/unexpected"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }





}
