package de.th.koeln.finanzdatenservice.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Abstrakte Basisklasse für Entitäten im Finanzdatendienst.
 * Diese Klasse definiert gemeinsame Eigenschaften und Verhalten für alle Entitäten,
 * die Finanzdaten darstellen.
 *
 * <p>Die Annotation {@link MappedSuperclass} gibt an, dass diese Klasse nicht direkt in der Datenbank
 * gespeichert wird, sondern die gemeinsamen Felder und Methoden von Unterklassen erben lässt.</p>
 */
@MappedSuperclass
public abstract class AbstraktEntitaet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, updatable = false)
    private LocalDateTime erstellerZeitstempel;

    @Column(nullable = false)
    private LocalDateTime bearbeiterZeitstempel;

    @Column(nullable = false)
    private String benutzerID;

    @Column(nullable = false)
    private Long kontoId;

    @Column(nullable = false)
    private BigDecimal betrag;

    /**
     * Standardkonstruktor.
     */
    protected AbstraktEntitaet() {
    }

    // Getter und Setter für alle Felder

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBenutzerID() {
        return this.benutzerID;
    }

    public void setBenutzerID(String benutzerID) {
        this.benutzerID = benutzerID;
    }

    public Long getKontoId() {
        return kontoId;
    }

    public void setKontoId(Long kontoId) {
        this.kontoId = kontoId;
    }

    public BigDecimal getBetrag() {
        return this.betrag;
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }

    public LocalDateTime getErstellerZeitstempel() {
        return erstellerZeitstempel;
    }

    public LocalDateTime getBearbeiterZeitstempel() {
        return bearbeiterZeitstempel;
    }

    /**
     * Methode, die vor dem Einfügen der Entität aufgerufen wird, um den Ersteller- und Bearbeiterzeitstempel zu setzen.
     */
    @PrePersist
    public void revesioniere() {
        this.erstellerZeitstempel = LocalDateTime.now();
        this.bearbeiterZeitstempel = LocalDateTime.now();
    }

    /**
     * Methode, die vor der Aktualisierung der Entität aufgerufen wird, um den Bearbeiterzeitstempel zu aktualisieren.
     */
    @PreUpdate
    public void setBearbeiterZeitstempel() {
        this.bearbeiterZeitstempel = LocalDateTime.now();
    }
}
