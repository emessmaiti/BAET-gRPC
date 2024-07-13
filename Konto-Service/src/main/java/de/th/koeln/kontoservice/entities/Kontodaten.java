package de.th.koeln.kontoservice.entities;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entität für Kontodaten.
 *
 * <p>Diese Klasse repräsentiert die Kontodaten eines Benutzers und enthält Informationen wie den Kontostand,
 * die Benutzer-ID und Zeitstempel für die Erstellung und Bearbeitung.</p>
 */
@Entity
public class Kontodaten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String benutzerId;

    @Version
    private Long version;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDateTime erstellerZeitstempel;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = true)
    private LocalDateTime bearbeiterZeitstempel;

    private BigDecimal kontostand = BigDecimal.ZERO;

    /**
     * Standardkonstruktor.
     */
    public Kontodaten() {}

    // Getter und Setter

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getKontostand() {
        return kontostand;
    }

    public void setKontostand(BigDecimal kontostand) {
        this.kontostand = kontostand;
    }

    public String getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(String benutzerId) {
        this.benutzerId = benutzerId;
    }

    /**
     * Setzt die Ersteller- und Bearbeiter-Zeitstempel vor dem Einfügen.
     */
    @PrePersist
    public void revesioniere() {
        this.erstellerZeitstempel = LocalDateTime.now();
        this.bearbeiterZeitstempel = LocalDateTime.now();
    }

    /**
     * Aktualisiert den Bearbeiter-Zeitstempel vor dem Update.
     */
    @PreUpdate
    public void setBearbeiterZeitstempel() {
        this.bearbeiterZeitstempel = LocalDateTime.now();
    }
}
