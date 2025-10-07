package com.github.martinalexis.course_management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "Course Management API",
                description = """
                        ## About the Project
                        The Course Management API is a robust backend solution for the comprehensive management of an e-learning platform. It allows instructors and students to interact in an educational ecosystem where content can be created, published, searched, and consumed efficiently and securely.

                        ---

                        ### What does the API allow you to do?
                        - **Create and manage courses:** Instructors (with the `TEACHER` role) can create, update, and delete their own courses, defining titles and descriptions.
                        - **Manage educational content (Lessons):** Within each course, instructors can add, modify, and delete lessons to structure their teaching material.
                        - **Enroll users in courses:** The system allows users to be enrolled in courses with specific roles (`TEACHER` or `STUDENT`). A user's role is course-specific, meaning they can be a `STUDENT` in one course and a `TEACHER` in another, which controls their access and permissions for each course individually.
                        - **Publish and manage reviews:** Students (with the `STUDENT` role) can post reviews and ratings for the courses they are enrolled in, fostering feedback and community.
                        - **Explore and search for courses:** Users can search for available courses on the platform, facilitating the discovery of new content.
                        - **Authentication and user management:** It supports a secure authentication system with registration and login via email/password, as well as social login with Google. Endpoint security is managed with JWT.
                        """,
                version = "1.0.0",
                contact = @Contact(
                        name = "Alexis Martin",
                        url = "https://github.com/martin-alexis"
                )

        ),
        servers = {
                @Server(
                        description = "Development Server",
                        url = "https://course-management-dev.onrender.com"
                ),
                @Server(
                        description = "Staging Server",
                        url = "https://course-management-stg.onrender.com"
                ),
                @Server(
                        description = "Production Server",
                        url = "https://course-management-prod.onrender.com"
                )
        },
        security = @SecurityRequirement(
                name = "Bearer Authentication"
        )
)
@SecurityScheme(
        name = "Bearer Authentication",
        description = "A JWT token is required to access protected endpoints. " +
                "Obtain the token from the login endpoint and paste it here in the format 'Bearer {token}'.",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {}