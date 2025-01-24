package com.selflearntech.techblogbackend.exception;

public class RefreshTokenException extends RuntimeException{
    public RefreshTokenException(String message) {
        super(message);
    }
}
