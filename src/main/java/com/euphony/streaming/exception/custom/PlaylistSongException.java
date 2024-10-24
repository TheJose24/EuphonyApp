package com.euphony.streaming.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PlaylistSongException extends RuntimeException {

  private final HttpStatus httpStatus;

  public PlaylistSongException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public PlaylistSongException(String message, Throwable cause, HttpStatus httpStatus) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }

  public PlaylistSongException(Throwable cause, HttpStatus httpStatus) {
    super(cause);
    this.httpStatus = httpStatus;
  }
}