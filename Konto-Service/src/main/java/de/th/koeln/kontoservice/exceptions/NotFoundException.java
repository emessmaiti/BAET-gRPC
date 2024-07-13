package de.th.koeln.kontoservice.exceptions;
/**
 * Ausnahme für nicht gefundene Ressourcen.
 *
 * <p>Diese Ausnahme wird ausgelöst, wenn eine angeforderte Ressource nicht gefunden wird.
 * Sie erweitert die RuntimeException und kann mit einer benutzerdefinierten Fehlermeldung initialisiert werden.</p>
 */
public class NotFoundException extends RuntimeException {

    /**
     * Konstruktor zur Initialisierung der Ausnahme mit einer Fehlermeldung.
     *
     * @param message Die Fehlermeldung.
     */
    public NotFoundException(String message) {
        super(message);
    }
}