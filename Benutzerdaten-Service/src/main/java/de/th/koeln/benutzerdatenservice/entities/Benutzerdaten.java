package de.th.koeln.benutzerdatenservice.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Die Klasse Benutzerdaten repräsentiert die Entität für Benutzerdaten.
 *
 * <p>Die Annotation {@link Entity} markiert diese Klasse als eine JPA-Entität, die in einer relationalen
 * Datenbank gespeichert wird. Die Annotation {@link Id} markiert das Primärschlüsselattribut,
 * und {@link GeneratedValue} definiert die Generierungsstrategie für diesen Schlüssel.</p>
 *
 * <p>Zusätzliche Annotationen wie {@link Version}, {@link Temporal}, {@link Enumerated} und {@link Column}
 * werden verwendet, um die Felder dieser Klasse weiter zu konfigurieren und deren Verhalten in der Datenbank zu steuern.</p>
 */
@Entity
public class Benutzerdaten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Version
    private Long version;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String vorname;
    private String nachname;
    private String email;

    @Enumerated(EnumType.STRING)
    private Geschlecht geschlecht;

    private String sub;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime letzteAnmeldung;

    /**
     * Standardkonstruktor.
     */
    public Benutzerdaten() {
    }

    // Getter und Setter für alle Felder

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public LocalDateTime getLetzteAnmeldung() {
        return letzteAnmeldung;
    }

    public void setLetzteAnmeldung(LocalDateTime letzteAnmeldung) {
        this.letzteAnmeldung = letzteAnmeldung;
    }

    /**
     * Methode, die vor dem Einfügen oder Aktualisieren der Entität aufgerufen wird,
     * um den Zeitstempel zu aktualisieren.
     */
    @PrePersist
    protected void revesioniere() {
        timestamp = LocalDateTime.now();
        letzteAnmeldung = LocalDateTime.now();
    }

    /**
     * Methode, die vor dem Aktualisieren der Entität aufgerufen wird,
     * um den Zeitstempel zu aktualisieren.
     */
    @PreUpdate
    protected void revesioniereUpdate() {
        timestamp = LocalDateTime.now();
    }
}

