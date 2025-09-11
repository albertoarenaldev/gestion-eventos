package es.cic.curso25.back.controlleradvice;

public class TipoEventoExistenteException extends RuntimeException {

    public TipoEventoExistenteException(String message) {
        super(message);
    }
}
