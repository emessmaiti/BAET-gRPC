package de.th.koeln.finanzdatenservice.entities;

/**
 * Enum-Klasse zur Kategorisierung von Ausgaben.
 * Diese Klasse definiert die möglichen Kategorien, die für
 * Ausgaben verwendet werden können.
 */
public enum AusgabeKategorie {
    LEBENSMITTEL("Lebensmittel"),
    MIETE("Miete"),
    TRANSPORT("Transport"),
    UNTERHALTUNG("Unterhaltung"),
    BILDUNG("Bildung"),
    GESUNDHEIT("Gesundheit"),
    VERSICHERUNGEN("Versicherungen"),
    KLEIDUNG("Kleidung"),
    REISEN("Reisen"),
    FREIZEIT("Freizeit"),
    INVESTITIONEN("Investitionen"),
    SONSTIGES("Sonstiges");

    private final String beschreibung;

    AusgabeKategorie(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    /**
     * Gibt die Beschreibung der Kategorie zurück.
     *
     * @return Die Beschreibung der Kategorie
     */
    public String getBeschreibung() {
        return beschreibung;
    }
}
