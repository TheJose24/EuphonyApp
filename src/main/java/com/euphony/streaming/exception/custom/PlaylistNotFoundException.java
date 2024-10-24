package com.euphony.streaming.exception.custom;

public class PlaylistNotFoundException extends RuntimeException {

  public PlaylistNotFoundException() {
    super();
  }

  public PlaylistNotFoundException(String message) {
    super(message);
  }

  public PlaylistNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public PlaylistNotFoundException(Throwable cause) {
    super(cause);
  }
}