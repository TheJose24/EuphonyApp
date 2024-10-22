package com.euphony.streaming.exception.custom;

// Excepción personalizada para errores específicos en operaciones de playlist
public class PlaylistException extends RuntimeException {

    // Constructor por defecto
    public PlaylistException() {
        super();
    }

    // Constructor que acepta un mensaje de error
    public PlaylistException(String message) {
        super(message);
    }

    // Constructor que acepta un mensaje y la causa del error
    public PlaylistException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor que acepta solo la causa del error
    public PlaylistException(Throwable cause) {
        super(cause);
    }
}