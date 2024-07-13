package de.th.koeln.benutzerdatenservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Die GlobalExceptionHandler-Klasse behandelt global alle Ausnahmen im Benutzerdatenservice.
 *
 * <p>Diese Klasse verwendet {@link ControllerAdvice}, um eine zentrale Ausnahmebehandlung für
 * den gesamten Anwendungskontext bereitzustellen. Spezifische Ausnahmebehandlungslogik wird durch
 * Methoden mit der {@link ExceptionHandler}-Annotation definiert.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Behandelt Ausnahmen vom Typ {@link BenutzerdatenExceptionHandler}.
     *
     * @param exception Die aufgetretene Ausnahme.
     * @return Eine {@link ResponseEntity} mit dem Fehlerstatus und der Fehlermeldung.
     */
    @ExceptionHandler(BenutzerdatenExceptionHandler.class)
    public ResponseEntity<?> handleBenutzerNotFound(BenutzerdatenExceptionHandler exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
