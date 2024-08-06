package de.th.koeln.benachrichtigungservice.dtos;

/**
 * Data Transfer Object (DTO) für Benutzerdaten.
 *
 * Diese Klasse repräsentiert die Benutzerdaten, die zwischen den Schichten
 * der Anwendung übertragen werden.
 */
public class BenutzerDTO {

    private String benutzerID;
    private String email;

    /**
     * Standardkonstruktor.
     */
    public BenutzerDTO () {}

    //Getter und Setter

    public String getBenutzerID() {
        return benutzerID;
    }

    public void setBenutzerID(String benutzerID) {
        this.benutzerID = benutzerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
