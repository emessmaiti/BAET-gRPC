package de.th.koeln.kontoservice.dto;

/**
 * Data Transfer Object (DTO) für Finanzdaten.
 *
 * Diese Klasse repräsentiert die Finanzdaten, die zwischen den Schichten der Anwendung
 * übertragen werden.
 */
public class FinanzdatenDto {

    private String benutzerID;
    private String kategorie;
    private String bezeichnung;
    private String beschreibung;
    private String datum;
    private String budget;
    private String betrag;

    //Getter und Setter

    public String getBenutzerID() {
        return benutzerID;
    }

    public void setBenutzerID(String benutzerID) {
        this.benutzerID = benutzerID;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBetrag() {
        return betrag;
    }

    public void setBetrag(String betrag) {
        this.betrag = betrag;
    }
}

