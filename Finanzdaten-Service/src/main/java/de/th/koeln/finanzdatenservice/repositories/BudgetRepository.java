package de.th.koeln.finanzdatenservice.repositories;

import de.th.koeln.finanzdatenservice.entities.Budget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * Das Repository-Interface für die Entität Budget.
 *
 * <p>Dieses Interface erweitert {@link BaseRepository} und bietet spezifische Abfragen für Budgets</p>
 */
public interface BudgetRepository extends BaseRepository<Budget> {
    /**
     * Findet Budgets eines Benutzers anhand der Benutzer-ID.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return Eine Menge von Budgets des Benutzers.
     */
    @Query("SELECT b FROM Budget b WHERE b.benutzerID = :benutzerID")
    Set<Budget> findBudgetsByBenutzerID(@Param("benutzerID") String benutzerID);

}
