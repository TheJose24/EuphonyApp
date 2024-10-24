package com.euphony.streaming.exception.advice;

import com.euphony.streaming.exception.custom.PlaylistCreationException;
import com.euphony.streaming.exception.custom.PlaylistDeletionException;
import com.euphony.streaming.exception.custom.PlaylistNotFoundException;
import com.euphony.streaming.exception.custom.PlaylistUpdateException;
import com.euphony.streaming.exception.custom.PlaylistSongException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerPlaylist
{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlaylistCreationException.class)
    public ResponseEntity<String> handlePlaylistCreationException(PlaylistCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(PlaylistNotFoundException.class)
    public ResponseEntity<String> handlePlaylistNotFoundException(PlaylistNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlaylistUpdateException.class)
    public ResponseEntity<String> handlePlaylistUpdateException(PlaylistUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(PlaylistDeletionException.class)
    public ResponseEntity<String> handlePlaylistDeletionException(PlaylistDeletionException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(PlaylistSongException.class)
    public ResponseEntity<String> handlePlaylistSongException(PlaylistSongException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("Error interno del servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}