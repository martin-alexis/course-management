# =================================================================
# Multi-Stage Dockerfile for Spring Boot Application
# =================================================================

# --- Build Stage ---
# Uses Maven with Java to compile the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# This argument receives the profile ('prod', 'stg', 'dev') from the 'docker build' command.
# If not provided, it defaults to 'dev'.
ARG PROFILE=dev

WORKDIR /build

# Copy only pom.xml first to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and package the application
COPY src ./src

# Use the PROFILE argument to select the correct Maven profile with the -P flag.
# This ensures the correct application-{profile}.properties is packaged.
RUN mvn clean package -P${PROFILE} -DskipTests


# --- Runtime Stage ---
# This stage creates the final, lightweight image.
FROM amazoncorretto:17

# These arguments are received from the 'docker build' command.
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app

# Copy the compiled JAR file from the build stage
COPY --from=build /build/target/course_management-*.jar app.jar

# Expose the internal application port - Render will auto-detect this
EXPOSE 8088

# Set environment variables based on the build arguments for use at runtime.
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

# This command correctly uses the profile at runtime.
CMD ["sh", "-c", "java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} app.jar"]