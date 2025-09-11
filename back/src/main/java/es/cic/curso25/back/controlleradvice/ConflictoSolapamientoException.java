package es.cic.curso25.back.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictoSolapamientoException extends RuntimeException {
    public ConflictoSolapamientoException(String message) {
        super(message);
    }
}
