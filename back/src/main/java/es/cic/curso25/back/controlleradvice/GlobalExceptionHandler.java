package es.cic.curso25.back.controlleradvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

       // excepciones de TipoEvento
    @ExceptionHandler(TipoEventoEmptyNameException.class)
    public ResponseEntity<String> handleTipoEventoEmptyNameException(TipoEventoEmptyNameException e) {
        logger.warn("Tipo de evento con nombre vacío: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error: " + e.getMessage());
    }

    @ExceptionHandler(TipoEventoExistenteException.class)
    public ResponseEntity<String> handleTipoEventoExistenteException(TipoEventoExistenteException e) {
        logger.warn("Intento de crear un tipo de evento que ya existe: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(TipoEventoConEventosException.class)
    public ResponseEntity<String> handleTipoEventoConEventosException(TipoEventoConEventosException e) {
        logger.warn("Intento de eliminar un tipo de evento con eventos asociados: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

   
}  