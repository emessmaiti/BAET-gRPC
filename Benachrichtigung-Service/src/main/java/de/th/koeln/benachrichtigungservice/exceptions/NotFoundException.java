package de.th.koeln.benachrichtigungservice.exceptions;

/**
 * Die NotFoundException-Klasse behandelt Ausnahmen, die auf eine nicht gefundene Ressource hinweisen.
 *
 * <p>Diese Klasse erweitert {@link RuntimeException} und bietet einen Konstruktor,
 * um eine Ausnahme mit einer spezifischen Fehlermeldung zu erzeugen.</p>
 */
public class NotFoundException extends RuntimeException {

    /**
     * Konstruktor zur Erstellung einer NotFoundException mit einer bestimmten Fehlermeldung.
     *
     * @param message Die Fehlermeldung.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
