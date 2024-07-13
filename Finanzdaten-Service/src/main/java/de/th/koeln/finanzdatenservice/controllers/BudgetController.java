package de.th.koeln.finanzdatenservice.controllers;

import de.th.koeln.finanzdatenservice.entities.Ausgabe;
import de.th.koeln.finanzdatenservice.entities.Budget;
import de.th.koeln.finanzdatenservice.exceptions.NotFoundException;
import de.th.koeln.finanzdatenservice.services.BaseService;
import de.th.koeln.finanzdatenservice.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * REST-Controller zur Verwaltung von Budgets.
 *
 * <p>Dieser Controller erweitert {@link BaseController} und bietet Endpunkte zur Verwaltung von Budgets,
 * einschließlich des Hinzufügens und Entfernens von Ausgaben sowie des Abrufens von Budgets für den aktuellen Monat.</p>
 */
@RestController
@RequestMapping("/api/budget")
public class BudgetController extends BaseController<Budget> {

    @Autowired
    protected BudgetService service;

    /**
     * Konstruktor zur Initialisierung des BaseService.
     *
     * @param baseService Der Service zur Verwaltung der Budgets.
     */
    protected BudgetController(BaseService<Budget> baseService) {
        super(baseService);
    }

    /**
     * Holt alle Budgets eines Benutzers für den aktuellen Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Eine Menge von Budgets des Benutzers für den aktuellen Monat.
     */
    @GetMapping("/{benutzerId}")
    public Set<Budget> getAlleBudgetsAktuellesMonat(@PathVariable String benutzerId) {
        return this.service.getBudgetsAktuellesMonats(benutzerId);
    }

    /**
     * Fügt eine Ausgabe einem Budget hinzu.
     *
     * @param budgetId Die ID des Budgets.
     * @param ausgabe Die hinzuzufügende Ausgabe.
     * @return Das aktualisierte Budget als ResponseEntity.
     */
    @PostMapping("/{budgetId}/ausgaben")
    public ResponseEntity<Budget> addAusgabeToBudget(@PathVariable Long budgetId, @RequestBody Ausgabe ausgabe) {
        Budget budget = service.findById(budgetId).orElseThrow(() -> new NotFoundException("Budget not found"));
        ausgabe.setBudget(budget);
        Budget updatedBudget = service.addAusgabeToBudget(ausgabe);
        return ResponseEntity.ok(updatedBudget);
    }

    /**
     * Entfernt eine Ausgabe aus einem Budget.
     *
     * @param budgetId Die ID des Budgets.
     * @param ausgabeId Die ID der zu entfernenden Ausgabe.
     * @return Das aktualisierte Budget als ResponseEntity.
     */
    @DeleteMapping("/{budgetId}/ausgaben/{ausgabeId}")
    public ResponseEntity<Budget> removeAusgabeFromBudget(@PathVariable Long budgetId, @PathVariable Long ausgabeId) {
        Budget budget = service.findById(budgetId).orElseThrow(() -> new NotFoundException("Budget not found"));
        Ausgabe ausgabe = budget.getAusgaben().stream().filter(a -> a.getId().equals(ausgabeId)).findFirst().orElseThrow(() -> new NotFoundException("Ausgabe not found"));
        Budget updatedBudget = service.removeAusgabeFromBudget(ausgabe);
        return ResponseEntity.ok(updatedBudget);
    }

    /**
     * Holt alle Budgets eines Benutzers für den aktuellen Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Eine Menge von Budgets des Benutzers für den aktuellen Monat als ResponseEntity.
     */
    @GetMapping
    public ResponseEntity<Set<Budget>> getBudgetsAktuellesMonats(@RequestParam String benutzerId) {
        Set<Budget> budgets = service.getBudgetsAktuellesMonats(benutzerId);
        return ResponseEntity.ok(budgets);
    }

    /**
     * Holt ein Budget anhand der ID.
     *
     * @param budgetId Die ID des Budgets.
     * @return Das gefundene Budget als ResponseEntity.
     */
    @GetMapping("/{budgetId}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable Long budgetId) {
        Budget budget = service.findById(budgetId).orElseThrow(() -> new NotFoundException("Budget not found"));
        return ResponseEntity.ok(budget);
    }
}
