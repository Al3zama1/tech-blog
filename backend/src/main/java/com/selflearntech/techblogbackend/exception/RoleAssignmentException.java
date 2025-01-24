package com.selflearntech.techblogbackend.exception;

public class RoleAssignmentException extends RuntimeException {
    public RoleAssignmentException(String message, String role) {
        super(String.format("%s: %s", message, role));
    }
}
