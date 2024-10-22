package com.euphony.streaming.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserUpdateException extends RuntimeException {
    private final HttpStatus httpStatus;

    public UserUpdateException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public UserUpdateException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public UserUpdateException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }
}
