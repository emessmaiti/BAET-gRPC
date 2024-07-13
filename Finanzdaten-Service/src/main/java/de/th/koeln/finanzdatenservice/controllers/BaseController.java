package de.th.koeln.finanzdatenservice.controllers;

import de.th.koeln.finanzdatenservice.entities.AbstraktEntitaet;
import de.th.koeln.finanzdatenservice.services.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

/**
 * Abstrakte Basisklasse für REST-Controller, die Entitäten verwalten.
 *
 * <p>Diese Klasse bietet allgemeine CRUD-Operationen für Entitäten, die von {@link AbstraktEntitaet} erben,
 * und nutzt den {@link BaseService} zur Ausführung der Geschäftslogik.</p>
 *
 * @param <T> Der Entitätstyp, der von AbstraktEntitaet erbt.
 */
public abstract class BaseController<T extends AbstraktEntitaet>  {

    protected final BaseService<T> baseService;

    /**
     * Konstruktor zur Initialisierung des BaseService.
     *
     * @param baseService Der Service zur Verwaltung der Entität.
     */
    protected BaseController(BaseService<T> baseService) {
        this.baseService = baseService;
    }

    /**
     * Erstellt eine neue Entität.
     *
     * @param entitaet Die zu erstellende Entität.
     * @param jwt Der JWT-Token des authentifizierten Benutzers.
     * @return Die erstellte Entität als ResponseEntity.
     */
    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entitaet, @AuthenticationPrincipal Jwt jwt) {
        try {
            String benutzerId = jwt.getSubject();
            entitaet.setBenutzerID(benutzerId);
            T createdEntity = baseService.save(entitaet);
            return ResponseEntity.ok(createdEntity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Aktualisiert eine bestehende Entität.
     *
     * @param id Die ID der zu aktualisierenden Entität.
     * @param entitaet Die aktualisierten Entitätsdaten.
     * @return Die aktualisierte Entität.
     */
    @PutMapping("/{id}")
    public T update(@PathVariable Long id, @RequestBody T entitaet) {
        entitaet.setId(id);
        return baseService.save(entitaet);
    }

    /**
     * Löscht eine Entität.
     *
     * @param id Die ID der zu löschenden Entität.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        baseService.delete(id);
    }

    /**
     * Findet eine Entität anhand der ID.
     *
     * @param id Die ID der zu findenden Entität.
     * @return Eine optionale Entität.
     */
    @GetMapping("/{id}")
    public Optional<T> findById(@PathVariable Long id) {
        return baseService.findById(id);
    }

    /**
     * Findet alle Entitäten eines Benutzers.
     *
     * @param jwt Der JWT-Token des authentifizierten Benutzers.
     * @return Eine Iterable von Entitäten des Benutzers.
     */
    @GetMapping("/alle")
    public Iterable<T> findAll(@AuthenticationPrincipal Jwt jwt) {
        String sub = jwt.getSubject();
        return baseService.findAllByBenutzerId(sub);
    }

    /**
     * Findet alle Entitäten eines bestimmten Benutzers anhand der Benutzer-ID.
     *
     * @param sub Die Benutzer-ID.
     * @return Eine Iterable von Entitäten des Benutzers.
     */
    @GetMapping("/all/{sub}")
    public Iterable<T> findAll(@PathVariable String sub) {
        return baseService.findAllByBenutzerId(sub);
    }

    /**
     * Findet alle Entitäten eines bestimmten Kontos anhand der Konto-ID.
     *
     * @param kontoId Die Konto-ID.
     * @return Eine Menge von Entitäten des Kontos.
     */
    @GetMapping("/all/konto/{kontoId}")
    public Set<T> findAllByKontoId(@PathVariable Long kontoId) {
        return baseService.findAllByKontoId(kontoId);
    }
}
