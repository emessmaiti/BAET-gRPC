package de.th.koeln.finanzdatenservice.repositories;

import de.th.koeln.finanzdatenservice.entities.AbstraktEntitaet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

/**
 * Das Basis-Repository-Interface für Entitäten, die von {@link AbstraktEntitaet} erben.
 *
 * <p>Dieses Interface definiert allgemeine Datenbankoperationen für alle Entitäten, die von
 * der AbstraktEntitaet-Klasse erben. Es enthält benutzerspezifische Abfragen und Methoden
 * zum Abrufen und Überprüfen von Entitäten basierend auf Benutzer- und Konto-IDs.</p>
 *
 * @param <T> Der Entitätstyp, der von AbstraktEntitaet erbt.
 */

@NoRepositoryBean
public interface BaseRepository<T extends AbstraktEntitaet> extends JpaRepository<T, Long> {

    /**
     * Findet alle Entitäten eines bestimmten Benutzers anhand der Benutzer-ID.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return Eine Menge von Entitäten des Benutzers.
     */
    @Query("SELECT t FROM #{#entityName} t WHERE t.benutzerID = :benutzerID")
    Set<T> findEntityByBenutzerID(@Param("benutzerID") String benutzerID);

    /**
     * Findet die Benutzer-ID einer bestimmten Entität.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return Die Benutzer-ID.
     */
    @Query("SELECT DISTINCT t.benutzerID FROM #{#entityName} t WHERE t.benutzerID = :benutzerID ")
    String findBenutzer(@Param("benutzerID") String benutzerID);

    /**
     * Überprüft, ob eine Entität mit der angegebenen Benutzer-ID existiert.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return true, wenn eine Entität mit der Benutzer-ID existiert, ansonsten false.
     */
    @Query("SELECT COUNT (t.benutzerID) > 0 FROM #{#entityName} t WHERE t.benutzerID = :benutzerID ")
    boolean existsBenutzer(@Param("benutzerID") String benutzerID);

    /**
     * Findet alle Entitäten eines bestimmten Benutzers anhand der Benutzer-ID.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return Eine Menge von Entitäten des Benutzers.
     */
    Set<T> findAllByBenutzerID(String benutzerID);

    /**
     * Findet eine Entität anhand der Benutzer-ID.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return Eine optionale Entität des Benutzers.
     */
    Optional<T> findByBenutzerID(String benutzerID);

    /**
     * Überprüft, ob eine Entität mit der angegebenen Benutzer-ID existiert.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return true, wenn eine Entität mit der Benutzer-ID existiert, ansonsten false.
     */
    boolean existsByBenutzerID(String benutzerID);

    /**
     * Findet alle Entitäten eines bestimmten Kontos anhand der Konto-ID.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Menge von Entitäten des Kontos.
     */
    Set<T> findAllByKontoId(Long kontoId);

    /**
     * Findet Entity eines bestimmten Kontos anhand der Konto-ID.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Menge von Entitäten des Kontos.
     */
    T findByKontoId(Long kontoId);
}
