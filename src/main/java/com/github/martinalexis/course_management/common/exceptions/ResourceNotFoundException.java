package com.github.martinalexis.course_management.common.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " with id " + id + " was not found");
    }
}
