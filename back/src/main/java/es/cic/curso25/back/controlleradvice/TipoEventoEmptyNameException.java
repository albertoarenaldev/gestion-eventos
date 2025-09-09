package es.cic.curso25.back.controlleradvice;

public class TipoEventoEmptyNameException extends RuntimeException {

    public TipoEventoEmptyNameException(String message) {
        super(message);
    }

    public TipoEventoEmptyNameException(String message, Throwable cause) {
        super(message, cause);
    }

}
