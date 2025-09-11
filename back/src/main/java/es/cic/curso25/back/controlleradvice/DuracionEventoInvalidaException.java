package es.cic.curso25.back.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuracionEventoInvalidaException extends RuntimeException {
    public DuracionEventoInvalidaException(String message) {
        super(message);
    }
}
