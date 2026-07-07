package es.cic.curso25.back.controlleradvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(TipoEventoNotFoundException.class)
    public ResponseEntity<String> handleTipoEventoNotFoundException(TipoEventoNotFoundException e) {
        logger.warn("Tipo de evento no encontrado: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
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

    @ExceptionHandler(DuracionIncoherenteException.class)
    public ResponseEntity<String> handleDuracionIncoherenteException(DuracionIncoherenteException e) {
        logger.warn("Error de validación de duración: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(DuracionEventoInvalidaException.class)
    public ResponseEntity<String> handleDuracionEventoInvalidaException(DuracionEventoInvalidaException e) {
        logger.warn("Error de validación de duración de evento: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(ConflictoSolapamientoException.class)
    public ResponseEntity<String> handleConflictoSolapamientoException(ConflictoSolapamientoException e) {
        logger.warn("Conflicto de solapamiento de evento: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }


}
