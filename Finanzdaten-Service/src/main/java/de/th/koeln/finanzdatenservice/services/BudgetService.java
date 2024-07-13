package de.th.koeln.finanzdatenservice.services;

import de.th.koeln.finanzdatenservice.entities.Ausgabe;
import de.th.koeln.finanzdatenservice.entities.Budget;
import de.th.koeln.finanzdatenservice.exceptions.NotFoundException;
import de.th.koeln.finanzdatenservice.repositories.BaseRepository;
import de.th.koeln.finanzdatenservice.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

/**
 * Der Service für die Verwaltung von Budgets.
 *
 * <p>Dieser Service erweitert {@link BaseService} und bietet zusätzliche Methoden zur spezifischen Verwaltung von Budgets.</p>
 */
@Service
public class BudgetService extends BaseService<Budget> {

    protected BudgetRepository repository;

    /**
     * Konstruktor zur Initialisierung des Repositories.
     *
     * @param repository Das Repository zur Verwaltung der Budgets.
     */
    @Autowired
    protected BudgetService(BaseRepository<Budget> repository) {
        super(repository);
        this.repository = (BudgetRepository) repository;
    }

    /**
     * Speichert ein Budget und setzt den Restbetrag und den Fortschritt.
     *
     * @param budget Das zu speichernde Budget.
     * @return Das gespeicherte Budget.
     */
    @Override
    @Transactional
    public Budget save(Budget budget) {
        if (budget.getKontoId() == null) {
            throw new IllegalArgumentException("kontoId cannot be null");
        }

        budget.setRestBetrag(budget.getBetrag());
        budget.setProgress(BigDecimal.ZERO);
        return super.save(budget);
    }

    /**
     * Fügt eine Ausgabe einem Budget hinzu und aktualisiert den Restbetrag und den Fortschritt.
     *
     * @param ausgabe Die hinzuzufügende Ausgabe.
     * @return Das aktualisierte Budget.
     */
    @Transactional
    public Budget addAusgabeToBudget(Ausgabe ausgabe) {
        Budget budget = repository.findById(ausgabe.getBudget().getId())
                .orElseThrow(() -> new NotFoundException("Budget not found"));
        budget.getAusgaben().add(ausgabe);
        ausgabe.setBudget(budget);
        updateRestBetrag(budget);
        budget.setProgress(calculateProgress(budget));
        return repository.save(budget);
    }

    /**
     * Entfernt eine Ausgabe aus einem Budget und aktualisiert den Restbetrag und den Fortschritt.
     *
     * @param ausgabe Die zu entfernende Ausgabe.
     * @return Das aktualisierte Budget.
     */
    @Transactional
    public Budget removeAusgabeFromBudget(Ausgabe ausgabe) {
        Budget budget = repository.findById(ausgabe.getBudget().getId())
                .orElseThrow(() -> new NotFoundException("Budget not found"));
        budget.getAusgaben().remove(ausgabe);
        ausgabe.setBudget(null);
        updateRestBetrag(budget);
        budget.setProgress(calculateProgress(budget));
        return repository.save(budget);
    }

    /**
     * Aktualisiert den Restbetrag eines Budgets basierend auf den Ausgaben.
     *
     * @param budget Das zu aktualisierende Budget.
     */
    public void updateRestBetrag(Budget budget) {
        BigDecimal totalAusgaben = budget.getAusgaben().stream()
                .map(Ausgabe::getBetrag)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        budget.setRestBetrag(budget.getBetrag().subtract(totalAusgaben));
        budget.setProgress(calculateProgress(budget));
    }

    /**
     * Berechnet den Fortschritt eines Budgets basierend auf den Ausgaben.
     *
     * @param budget Das Budget, für das der Fortschritt berechnet werden soll.
     * @return Der berechnete Fortschritt in Prozent.
     */
    private BigDecimal calculateProgress(Budget budget) {
        BigDecimal spentAmount = budget.getBetrag().subtract(budget.getRestBetrag());
        if (budget.getBetrag().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return spentAmount.divide(budget.getBetrag(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    /**
     * Holt die Budgets eines Benutzers für den aktuellen Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Eine Menge von Budgets des Benutzers für den aktuellen Monat.
     */
    public Set<Budget> getBudgetsAktuellesMonats(String benutzerId) {
        Set<Budget> budgets = this.repository.findBudgetsByBenutzerID(benutzerId);
        Set<Budget> budgetsAktuellesMonats = new HashSet<>();
        YearMonth aktuelleMonat = YearMonth.now();

        for (Budget budget : budgets) {
            YearMonth budgetMonat = YearMonth.from(budget.getStartDatum());
            if (budgetMonat.equals(aktuelleMonat)) {
                budget.setProgress(calculateProgress(budget));
                budgetsAktuellesMonats.add(budget);
            }
        }
        return budgetsAktuellesMonats;
    }
}
