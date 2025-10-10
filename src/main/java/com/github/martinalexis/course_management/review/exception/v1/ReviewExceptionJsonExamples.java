package com.github.martinalexis.course_management.review.exception.v1;

public class ReviewExceptionJsonExamples {

    public static final String STUDENT_ALREADY_REVIEWED_RESPONSE = """
                {
                  "type": "https://example.com/errors/forbidden-access",
                  "title": "Forbidden Access",
                  "status": 403,
                  "detail": "The student has already reviewed this course.",
                  "instance": "/api/v1/courses/123/reviews"
                }
            """;
    public static final String REVIEW_DOES_NOT_BELONG_TO_USER_RESPONSE = """
                {
                 "type": "https://example.com/errors/forbidden-access",
                 "title": "Forbidden Access",
                 "status": 403,
                 "detail": "The review does not belong to the current user.",
                 "instance": "/api/v1/courses/123/reviews"
                 }
            
            """;
}

