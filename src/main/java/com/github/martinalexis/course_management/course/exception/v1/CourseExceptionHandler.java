package com.github.martinalexis.course_management.course.exception.v1;

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
@RestControllerAdvice
public class CourseExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(TeacherCannotEnrollException.class)
    public ProblemDetail handleTeacherCannotEnroll(TeacherCannotEnrollException ex, WebRequest request) {
        log.warn("Cannot enroll teacher", ex);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Cannot enroll teacher");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/teacher-cannot-enroll"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }

    @ExceptionHandler(StudentAlreadyEnrolledException.class)
    public ProblemDetail handleStudentAlreadyEnrolled(StudentAlreadyEnrolledException ex, WebRequest request) {
        log.warn(ex.getMessage(), ex);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Student already enrolled");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/student-already--enrolled"));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        return problem;
    }

}
