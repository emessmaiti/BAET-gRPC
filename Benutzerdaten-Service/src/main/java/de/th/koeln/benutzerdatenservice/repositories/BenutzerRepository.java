package de.th.koeln.benutzerdatenservice.repositories;

import de.th.koeln.benutzerdatenservice.entities.Benutzerdaten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Das Repository-Interface für die Benutzerdaten-Entität.
 *
 * <p>Dieses Interface erweitert {@link JpaRepository} und bietet Methoden zum Abrufen und
 * Aktualisieren von Benutzerdaten in der Datenbank.</p>
 */
@Repository
public interface BenutzerRepository extends JpaRepository<Benutzerdaten, Long> {

    /**
     * Findet einen Benutzer anhand der E-Mail-Adresse.
     *
     * @param email Die E-Mail-Adresse des Benutzers.
     * @return Die Benutzerdaten des Benutzers.
     */
    Benutzerdaten findByEmail(String email);

    /**
     * Findet einen Benutzer anhand der Sub (Subject-ID).
     *
     * @param sub Die Subject-ID des Benutzers.
     * @return Die Benutzerdaten des Benutzers.
     */
    Benutzerdaten findBenutzerdatenBySub(String sub);

    /**
     * Aktualisiert das Datum und die Uhrzeit der letzten Anmeldung eines Benutzers.
     *
     * @param sub Die Subject-ID des Benutzers.
     * @param letzteAnmeldung Das neue Datum und die neue Uhrzeit der letzten Anmeldung.
     */
    @Modifying
    @Query("UPDATE Benutzerdaten b SET b.letzteAnmeldung = :letzteAnmeldung WHERE b.sub = :sub")
    void updateLastLogin(@Param("sub") String sub, @Param("letzteAnmeldung") LocalDateTime letzteAnmeldung);
}
