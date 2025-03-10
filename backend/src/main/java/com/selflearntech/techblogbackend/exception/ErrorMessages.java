package com.selflearntech.techblogbackend.exception;

public class ErrorMessages {
    public static final String PASSWORD_CONSTRAINT = "Password must be 8 to 15 characters long, include uppercase and lowercase letters, a number, and a special character (! @ # $ %)";
    public static final String VALIDATION_ERROR = "Validation error. Check 'errors' field for details";
    public static final String UNKNOWN_ERROR = "Unknown error occurred";
    public static final String ROLE_ASSIGNMENT_FAILURE = "Role assignment failure";
    public static final String EMAIL_TAKEN = "Email is taken, try a different email address";
    public static final String PASSWORDS_MUST_MATCH = "Registration passwords must match";
    public static final String INVALID_CREDENTIALS = "Check your credentials and try again";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public static final String INVALIDATED_REFRESH_TOKEN = "Invalidated refresh token";
    public static final String COOKIE_REFRESH_TOKEN_AND_DB_TOKEN_MISMATCH = "Cookie and db refresh tokens do not match";
    public static final String EXPIRED_REFRESH_TOKEN = "Expired refresh token";
    public static final String REFRESH_TOKEN_EXPIRATION_DATE_MISMATCH = "Refresh token expiration dates do not match";
    public static final String FAILED_TOKEN_VALIDATION = "Failed token validation";
    public static final String STORED_TOKEN_AND_COOKIE_REFRESH_TOKEN_USER_MISMATCH = "Stored toke and cookie refresh token user mismatch";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found";
    public static final String  ROLE_NOT_FOUND = "Role not found";
    public static final String NOT_FOUND = "Resource not found";
    public static final String MISSING_REQUIRED_COOKIE = "Missing required cookie";
    public static final String MISSING_REQUEST_BODY = "Missing required request body";
}
