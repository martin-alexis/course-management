# =================================================================
# Multi-Stage Dockerfile for Spring Boot Application
# =================================================================

# --- Build Stage ---
# Uses Maven with Java to compile the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build

# Copy only pom.xml first to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and package the application
COPY src ./src
RUN mvn clean package -DskipTests

# --- Runtime Stage ---

FROM amazoncorretto:17


ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app

# Copy compiled JAR file from build stage
COPY --from=build /build/target/course_management-*.jar app.jar

# Expose internal application port - Render will auto-detect this
EXPOSE 8088

# Set environment variables based on build arguments
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD ["sh", "-c", "java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} app.jar"]