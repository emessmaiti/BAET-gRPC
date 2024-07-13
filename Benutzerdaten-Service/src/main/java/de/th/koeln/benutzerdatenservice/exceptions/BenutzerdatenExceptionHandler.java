package de.th.koeln.benutzerdatenservice.exceptions;

/**
 * Die BenutzerdatenExceptionHandler-Klasse behandelt benutzerspezifische Ausnahmen im Benutzerdatenservice.
 *
 * <p>Diese Klasse erweitert {@link RuntimeException} und bietet Konstruktoren, um benutzerspezifische
 * Ausnahmen mit einer Fehlermeldung und optional einer zugrunde liegenden Ursache zu erzeugen.</p>
 */
public class BenutzerdatenExceptionHandler extends RuntimeException {

    /**
     * Konstruktor zur Erstellung einer Ausnahme mit einer bestimmten Fehlermeldung.
     *
     * @param message Die Fehlermeldung.
     */
    public BenutzerdatenExceptionHandler(String message) {
        super(message);
    }

    /**
     * Konstruktor zur Erstellung einer Ausnahme mit einer bestimmten Fehlermeldung und einer zugrunde liegenden Ursache.
     *
     * @param message Die Fehlermeldung.
     * @param cause   Die zugrunde liegende Ursache der Ausnahme.
     */
    public BenutzerdatenExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
}
