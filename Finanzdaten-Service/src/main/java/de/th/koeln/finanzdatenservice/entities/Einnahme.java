package de.th.koeln.finanzdatenservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

/**
 * Die Klasse Einnahme repräsentiert eine finanzielle Einnahme, die in der Anwendung verwaltet wird.
 * Sie erbt von der abstrakten Klasse {@link AbstraktEntitaet}, die gemeinsame Eigenschaften und Verhalten definiert.
 *
 * <p>Diese Klasse enthält spezifische Eigenschaften einer Einnahme wie Kategorie, Bezeichnung, Beschreibung und Datum.</p>
 */

@Entity
public class Einnahme extends AbstraktEntitaet {

    @Enumerated(EnumType.STRING)
    private EinnahmeKategorie einnahmeKategorie;

    private String bezeichnung;
    private String beschreibung;

    @Column(nullable = false)
    private LocalDate datum;

    /**
     * Standardkonstruktor.
     */
    public Einnahme() {
        super();
    }

    // Getter und Setter für alle Felder

    @NonNull
    public EinnahmeKategorie getEinnahmeKategorie() {
        return einnahmeKategorie;
    }

    public void setEinnahmeKategorie(@NonNull EinnahmeKategorie einnahmeKategorie) {
        this.einnahmeKategorie = einnahmeKategorie;
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

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}
