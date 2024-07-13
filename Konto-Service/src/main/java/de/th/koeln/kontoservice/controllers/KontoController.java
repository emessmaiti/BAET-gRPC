package de.th.koeln.kontoservice.controllers;

import de.th.koeln.kontoservice.entities.Kontodaten;
import de.th.koeln.kontoservice.services.KontodatenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST-Controller zur Verwaltung von Kontodaten.
 *
 * <p>Dieser Controller bietet Endpunkte zur Verwaltung von Kontodaten, einschließlich der Erstellung,
 * Aktualisierung, Löschung und Abfrage von Kontodaten sowie der Abfrage von Einnahmen und Ausgaben.</p>
 */
@RestController
@RequestMapping("/api/konto")
public class KontoController {

    private final KontodatenService service;

    /**
     * Konstruktor zur Initialisierung des Kontodaten-Services.
     *
     * @param service Der Kontodaten-Service.
     */
    @Autowired
    public KontoController(KontodatenService service) {
        this.service = service;
    }

    /**
     * Speichert Kontodaten.
     *
     * @param konto Die zu speichernden Kontodaten.
     * @return Die gespeicherten Kontodaten als ResponseEntity.
     */
    @PostMapping
    public ResponseEntity<Kontodaten> save(@RequestBody Kontodaten konto) {
        Kontodaten savedKonto = service.save(konto);
        return ResponseEntity.ok(savedKonto);
    }

    /**
     * Aktualisiert Kontodaten.
     *
     * @param kontoId Die ID der zu aktualisierenden Kontodaten.
     * @param konto   Die aktualisierten Kontodaten.
     * @return Die aktualisierten Kontodaten als ResponseEntity.
     */
    @PutMapping("/{kontoId}")
    public ResponseEntity<Kontodaten> update(@PathVariable long kontoId, @RequestBody Kontodaten konto) {
        Kontodaten updatedKonto = service.update(kontoId, konto);
        return ResponseEntity.ok(updatedKonto);
    }

    /**
     * Löscht Kontodaten.
     *
     * @param kontoId Die ID der zu löschenden Kontodaten.
     * @return Eine leere ResponseEntity.
     */
    @DeleteMapping("/{kontoId}")
    public ResponseEntity<Void> delete(@PathVariable long kontoId) {
        service.delete(kontoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Findet Kontodaten anhand der ID.
     *
     * @param kontoId Die ID der Kontodaten.
     * @return Die gefundenen Kontodaten als ResponseEntity.
     */
    @GetMapping("/{kontoId}")
    public ResponseEntity<Kontodaten> findById(@PathVariable long kontoId) {
        Optional<Kontodaten> konto = service.findKontoById(kontoId);
        return konto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}