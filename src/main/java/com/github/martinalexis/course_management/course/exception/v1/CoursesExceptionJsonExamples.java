package com.github.martinalexis.course_management.course.exception.v1;

public class CoursesExceptionJsonExamples {
    /**
     * Example response for when a user tries to modify or delete a course
     * they do not own (i.e., they are not the TEACHER).
     * This typically results in a 403 Forbidden status.
     * Corresponds to: InvalidCourseRoleException
     */
    public static final String USER_NOT_OWN_COURSE_RESPONSE = """
                {
                  "type": "https://example.com/errors/forbidden-access",
                  "title": "Forbidden Access",
                  "status": 403,
                  "detail": "The user is not authorized to perform this action on the specified course.",
                  "instance": "/api/v1/courses/123"
                }
            """;

    public static final String USER_NOT_ENROLLED_COURSE_RESPONSE = """
                {
                  "type": "https://example.com/errors/forbidden-access",
                  "title": "Forbidden Access",
                  "status": 403,
                  "detail": "User is not enrolled as a student in this course.",
                  "instance": "/api/v1/courses/123"
                }
            """;

    /**
     * Example response for when a user who is the TEACHER of a course
     * attempts to enroll in it as a student. This is a business rule violation.
     * This typically results in a 409 Conflict status.
     * Corresponds to: TeacherCannotEnrollException
     */
    public static final String TEACHER_CANNOT_ENROLL_RESPONSE = """
                {
                  "type": "https://example.com/errors/teacher-cannot-enroll",
                  "title": "Teacher cannot enroll",
                  "status": 409,
                  "detail": "A teacher cannot enroll in their own course.",
                  "instance": "/api/v1/courses/123/enroll"
                }
            """;

    /**
     * Example response for when a student tries to enroll in a course
     * in which they are already enrolled. This is a state conflict.
     * This typically results in a 409 Conflict status.
     * Corresponds to: StudentAlreadyEnrolledException
     */
    public static final String STUDENT_ALREADY_ENROLLED_RESPONSE = """
                {
                  "type": "https://example.com/errors/student-already-enrolled",
                  "title": "Student already enrolled",
                  "status": 409,
                  "detail": "The student is already enrolled in this course.",
                  "instance": "/api/v1/courses/123/enroll"
                }
            """;
}

