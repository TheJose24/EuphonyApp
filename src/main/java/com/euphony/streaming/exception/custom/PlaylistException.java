package com.euphony.streaming.exception.custom;

/**
 * Excepción personalizada para manejar errores relacionados con playlists.
 * Se utiliza para situaciones específicas de error en operaciones de playlist.
 */
public class PlaylistException extends RuntimeException {

    /**
     * Constructor por defecto.
     */
    public PlaylistException() {
        super();
    }

    /**
     * Constructor con mensaje de error.
     * @param message el mensaje detallado del error
     */
    public PlaylistException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje de error y causa.
     * @param message el mensaje detallado del error
     * @param cause la causa subyacente del error
     */
    public PlaylistException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor con causa.
     * @param cause la causa subyacente del error
     */
    public PlaylistException(Throwable cause) {
        super(cause);
    }
}