package de.th.koeln.finanzdatenservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Die GlobaleExceptionHandler-Klasse behandelt global alle Ausnahmen im Finanzdatendienst.
 *
 * <p>Diese Klasse verwendet {@link ControllerAdvice}, um eine zentrale Ausnahmebehandlung für
 * den gesamten Anwendungskontext bereitzustellen. Spezifische Ausnahmebehandlungslogik wird durch
 * Methoden mit der {@link ExceptionHandler}-Annotation definiert.</p>
 */
@ControllerAdvice
public class GlobaleExceptionHandler {

    /**
     * Behandelt Ausnahmen vom Typ {@link NotFoundException}.
     *
     * @param ex Die aufgetretene Ausnahme.
     * @return Eine {@link ResponseEntity} mit dem Fehlerstatus und der Fehlermeldung.
     */
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Behandelt Ausnahmen vom Typ {@link BadRequestException}.
     *
     * @param ex Die aufgetretene Ausnahme.
     * @return Eine {@link ResponseEntity} mit dem Fehlerstatus und der Fehlermeldung.
     */
    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Behandelt alle anderen Ausnahmen.
     *
     * @param ex Die aufgetretene Ausnahme.
     * @return Eine {@link ResponseEntity} mit dem Fehlerstatus und einer allgemeinen Fehlermeldung.
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("Ein unerwarteter Fehler ist aufgetreten: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
