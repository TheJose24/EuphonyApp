package com.euphony.streaming.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PlaylistCreationException extends RuntimeException {

    private final HttpStatus httpStatus;

    public PlaylistCreationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public PlaylistCreationException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public PlaylistCreationException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }
}