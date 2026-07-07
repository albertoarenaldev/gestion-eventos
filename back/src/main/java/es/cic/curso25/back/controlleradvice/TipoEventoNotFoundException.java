package es.cic.curso25.back.controlleradvice;

/**
 * Thrown when a TipoEvento lookup by id fails to find a matching entity.
 * The GlobalExceptionHandler maps this to HTTP 404 Not Found.
 */
public class TipoEventoNotFoundException extends RuntimeException {
    public TipoEventoNotFoundException(String message) {
        super(message);
    }
}
