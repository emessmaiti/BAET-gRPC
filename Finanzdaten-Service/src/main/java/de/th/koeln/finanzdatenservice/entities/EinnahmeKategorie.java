package de.th.koeln.finanzdatenservice.entities;
/**
 * Enum-Klasse zur Kategorisierung von Einnahmen.
 * Diese Klasse definiert die möglichen Kategorien, die für
 * Einnahmen verwendet werden können.
 */
public enum EinnahmeKategorie {

    GEHALT("Gehalt"),
    BAFOEG("Bafoeg"),
    GESCHENKE("Geschenke"),
    INVESTITIONEN("Investitionen");

    private final String beschreibung;


    /**
     * Konstruktor für die Kategorie.
     *
     * @param beschreibung Die Beschreibung der Kategorie
     */
    EinnahmeKategorie(String beschreibung) {
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
