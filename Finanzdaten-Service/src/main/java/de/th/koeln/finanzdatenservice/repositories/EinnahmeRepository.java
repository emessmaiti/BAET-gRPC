package de.th.koeln.finanzdatenservice.repositories;

import de.th.koeln.finanzdatenservice.entities.Einnahme;
import de.th.koeln.finanzdatenservice.entities.EinnahmeKategorie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * Das Repository-Interface für die Entität Einnahme.
 *
 * <p>Dieses Interface erweitert {@link BaseRepository} und bietet spezifische Abfragen für Einnahmen</p>
 */
public interface EinnahmeRepository extends BaseRepository<Einnahme> {
    /**
     * Findet Einnahmen eines Benutzers für einen bestimmten Monat.
     *
     * @param benutzerID Die ID des Benutzers.
     * @param monat Der Monat, für den die Einnahmen abgerufen werden sollen.
     * @return Eine Menge von Einnahmen des Benutzers für den angegebenen Monat.
     */
    @Query("SELECT e FROM Einnahme e WHERE e.benutzerID = :benutzerID AND MONTH(e.datum) = :monat ORDER BY e.datum DESC")
    Set<Einnahme> findEinnahmeByMonth(@Param("benutzerID") String benutzerID, @Param("monat") int monat);

    /**
     * Findet Einnahmen eines Kontos für einen bestimmten Monat.
     *
     * @param kontoId Die ID des Kontos.
     * @param monat Der Monat, für den die Einnahmen abgerufen werden sollen.
     * @return Eine Menge von Einnahmen des Kontos für den angegebenen Monat.
     */
    @Query("SELECT e FROM Einnahme e WHERE e.kontoId = :kontoId AND MONTH(e.datum) = :monat ORDER BY e.datum DESC")
    Set<Einnahme> findEinnahmeByMonth(@Param("kontoId") Long kontoId, @Param("monat") int monat);

    /**
     * Findet alle Einnahmen eines Benutzers und sortiert sie absteigend nach Datum.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return Eine Menge von Einnahmen des Benutzers sortiert nach Datum absteigend.
     */
    @Query("SELECT e FROM Einnahme e WHERE e.benutzerID = :benutzerID ORDER BY e.datum DESC")
    Set<Einnahme> findAllOrderByDatumDesc(@Param("benutzerID") String benutzerID);

    /**
     * Findet eine Einnahme anhand der Einnahmekategorie.
     *
     * @param kategorie Die Einnahmekategorie.
     * @return Die Einnahme der angegebenen Kategorie.
     */
    Einnahme findEinnahmeByEinnahmeKategorie(EinnahmeKategorie kategorie);
}
