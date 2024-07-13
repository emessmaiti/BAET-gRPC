package de.th.koeln.kontoservice.repositories;

import de.th.koeln.kontoservice.entities.Kontodaten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository für die Verwaltung von Kontodaten.
 *
 * <p>Dieses Interface erweitert {@link JpaRepository} und bietet Methoden zur Interaktion mit der Datenbank,
 * einschließlich einer benutzerdefinierten Abfrage zum Finden von Kontodaten anhand der Benutzer-ID.</p>
 */
public interface KontodatenRepository extends JpaRepository<Kontodaten, Long> {

    /**
     * Findet Kontodaten anhand der Benutzer-ID.
     *
     * <p>Diese Methode führt eine benutzerdefinierte Abfrage aus, um Kontodaten basierend auf der gegebenen Benutzer-ID abzurufen.</p>
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Die gefundenen Kontodaten.
     */
    @Query("SELECT k FROM Kontodaten k WHERE k.benutzerId = :benutzerId")
    Kontodaten findKontodatenByBenutzerId(@Param("benutzerId") String benutzerId);

}
