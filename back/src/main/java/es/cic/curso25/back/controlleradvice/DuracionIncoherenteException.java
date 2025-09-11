package es.cic.curso25.back.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuracionIncoherenteException extends RuntimeException {
    public DuracionIncoherenteException(String message) {
        super(message);
    }
}
