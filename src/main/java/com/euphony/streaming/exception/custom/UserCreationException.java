package com.euphony.streaming.exception.custom;

public class UserCreationException extends RuntimeException {

    public UserCreationException() {
        super();
    }

    public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreationException(Throwable cause) {
        super(cause);
    }
}
