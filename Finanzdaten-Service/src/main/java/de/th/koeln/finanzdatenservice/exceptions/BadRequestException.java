package de.th.koeln.finanzdatenservice.exceptions;

/**
 * Die BadRequestException-Klasse behandelt Ausnahmen, die auf eine ungültige Anfrage hinweisen.
 *
 * <p>Diese Klasse erweitert {@link RuntimeException} und bietet einen Konstruktor,
 * um eine Ausnahme mit einer spezifischen Fehlermeldung zu erzeugen.</p>
 */
public class BadRequestException extends RuntimeException {

    /**
     * Konstruktor zur Erstellung einer BadRequestException mit einer bestimmten Fehlermeldung.
     *
     * @param message Die Fehlermeldung.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
