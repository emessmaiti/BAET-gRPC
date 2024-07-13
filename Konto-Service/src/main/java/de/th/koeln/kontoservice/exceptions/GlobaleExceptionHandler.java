package de.th.koeln.kontoservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Globale Exception-Handler-Klasse zur Behandlung von Ausnahmen in der Anwendung.
 *
 * <p>Diese Klasse verwendet {@link ControllerAdvice}, um globale Ausnahmebehandlungslogik bereitzustellen.
 * Es definiert spezifische Handler-Methoden für verschiedene Arten von Ausnahmen, um geeignete HTTP-Statuscodes
 * und Fehlermeldungen zurückzugeben.</p>
 */
@ControllerAdvice
public class GlobaleExceptionHandler {

    /**
     * Handler für NotFoundException.
     *
     * @param e Die ausgelöste NotFoundException.
     * @return Eine ResponseEntity mit einer Fehlermeldung und dem HTTP-Status NOT_FOUND.
     */
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handler für BadRequestException.
     *
     * @param e Die ausgelöste BadRequestException.
     * @return Eine ResponseEntity mit einer Fehlermeldung und dem HTTP-Status BAD_REQUEST.
     */
    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Allgemeiner Handler für alle anderen Ausnahmen.
     *
     * @param e Die ausgelöste Ausnahme.
     * @return Eine ResponseEntity mit einer allgemeinen Fehlermeldung und dem HTTP-Status INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
