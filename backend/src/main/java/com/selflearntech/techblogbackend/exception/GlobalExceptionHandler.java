package com.selflearntech.techblogbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String TRACE = "trace";

    @Value("${selflearntech.trace}")
    private boolean printStackTrace;

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleUserExistsException(ConflictException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return buildErrorResponse(ex, ErrorMessages.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleRefreshTokenException(RefreshTokenException ex) {
        log.info(ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ErrorMessages.INVALID_REFRESH_TOKEN);
        ResponseCookie deleteRefreshTokenCookie = ResponseCookie.from("refresh-token", null).build();

        return ResponseEntity.status(errorResponse.getStatus())
                .header(HttpHeaders.SET_COOKIE, deleteRefreshTokenCookie.toString())
                .body(errorResponse);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NO_CONTENT.value(), ErrorMessages.VALIDATION_ERROR);
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RoleAssignmentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleRoleAssignmentException(RoleAssignmentException ex, WebRequest request) {
        log.error(ErrorMessages.ROLE_ASSIGNMENT_FAILURE, ex);
        return buildErrorResponse(ex, ErrorMessages.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    // handles all internal Spring Exceptions
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, @NonNull Object body, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
        log.error("Internal Spring Boot Exception", ex);
        return buildErrorResponse(ex, ErrorMessages.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtExceptions(Exception ex, WebRequest request) {
        log.error(ErrorMessages.UNKNOWN_ERROR, ex);
        return buildErrorResponse(ex, ErrorMessages.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(Arrays.toString(exception.getStackTrace()));
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value) && value.length > 0 && value[0].contentEquals("true");
    }


}
