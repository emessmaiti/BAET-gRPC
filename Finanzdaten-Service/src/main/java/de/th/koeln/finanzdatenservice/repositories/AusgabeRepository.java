package de.th.koeln.finanzdatenservice.repositories;

import de.th.koeln.finanzdatenservice.entities.Ausgabe;
import de.th.koeln.finanzdatenservice.entities.AusgabeKategorie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Set;

/**
 * Das Repository-Interface für die Entität Ausgabe.
 *
 * <p>Dieses Interface erweitert {@link BaseRepository} und bietet spezifische Abfragen für Ausgaben</p>
 */
public interface AusgabeRepository extends BaseRepository<Ausgabe> {

    /**
     * Findet eine Ausgabe basierend auf einem Datum zwischen zwei gegebenen Daten und der Benutzer-ID.
     *
     * @param from Das Startdatum.
     * @param to Das Enddatum.
     * @param benutzerID Die ID des Benutzers.
     * @return Die gefundene Ausgabe.
     */
    Ausgabe findAusgabeByDatumBetweenAndBenutzerID(LocalDate from, LocalDate to, String benutzerID);

    /**
     * Findet alle Ausgaben eines Benutzers und sortiert sie absteigend nach Datum.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return Eine Menge von Ausgaben des Benutzers, sortiert nach Datum absteigend.
     */
    @Query("SELECT a FROM Ausgabe a WHERE a.benutzerID = :benutzerID ORDER BY a.datum DESC")
    Set<Ausgabe> findAllOrderByDatumDesc(@Param("benutzerID") String benutzerID);

    /**
     * Findet Ausgaben eines Benutzers für einen bestimmten Monat.
     *
     * @param benutzerID Die ID des Benutzers.
     * @param monat Der Monat, für den die Ausgaben abgerufen werden sollen.
     * @return Eine Menge von Ausgaben des Benutzers für den angegebenen Monat.
     */
    @Query("SELECT a FROM Ausgabe a WHERE a.benutzerID = :benutzerID AND MONTH(a.datum) = :monat ORDER BY a.datum DESC")
    Set<Ausgabe> findAusgabenByMonat(@Param("benutzerID") String benutzerID, @Param("monat") int monat);

    /**
     * Findet Ausgaben eines Kontos für einen bestimmten Monat.
     *
     * @param kontoId Die ID des Kontos.
     * @param monat Der Monat, für den die Ausgaben abgerufen werden sollen.
     * @return Eine Menge von Ausgaben des Kontos für den angegebenen Monat.
     */
    @Query("SELECT a FROM Ausgabe a WHERE a.kontoId = :kontoId AND MONTH(a.datum) = :monat ORDER BY a.datum DESC")
    Set<Ausgabe> findAusgabenByMonat(@Param("kontoId") Long kontoId, @Param("monat") int monat);

    /**
     * Findet eine Ausgabe anhand der Ausgabenkategorie.
     *
     * @param kategorie Die Ausgabenkategorie.
     * @return Die Ausgabe der angegebenen Kategorie.
     */
    Ausgabe findAusgabeByAusgabeKategorie(AusgabeKategorie kategorie);
}
