package de.th.koeln.kontoservice.controllers;

import de.th.koeln.kontoservice.dto.FinanzdatenDto;
import de.th.koeln.kontoservice.entities.Kontodaten;
import de.th.koeln.kontoservice.services.KontodatenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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


    /**
     * Berechnet den Kontostand anhand der Konto-ID.
     *
     * @param kontoId Die Konto-ID.
     * @return Der berechnete Kontostand als ResponseEntity.
     */
    @GetMapping("/kontostand/{kontoId}")
    public ResponseEntity<BigDecimal> getKontostandByKontoId(@PathVariable Long kontoId) {
        BigDecimal kontostand = service.getKontoStandByKontoId(kontoId);
        return ResponseEntity.ok(kontostand);
    }

    /**
     * Holt alle Einnahmen eines Kontos anhand der Konto-ID.
     *
     * @param kontoId Die Konto-ID.
     * @return Eine Iterable von Einnahme-Daten-Transfer-Objekten (EinnahmeDTO) des Kontos als ResponseEntity.
     */
    @GetMapping("/einnahmen/{kontoId}")
    public ResponseEntity<Iterable<FinanzdatenDto>> getEinnahmen(@PathVariable Long kontoId) {
        Iterable<FinanzdatenDto> einnahmen = service.findAllEinnahmenByKontoId(kontoId);
        return ResponseEntity.ok(einnahmen);
    }

    /**
     * Holt alle Ausgaben eines Kontos anhand der Konto-ID.
     *
     * @param kontoId Die Konto-ID.
     * @return Eine Iterable von Ausgaben-Daten-Transfer-Objekten (AusgabenDTO) des Kontos als ResponseEntity.
     */
    @GetMapping("/ausgaben/{kontoId}")
    public ResponseEntity<Iterable<FinanzdatenDto>> getAusgaben(@PathVariable Long kontoId) {
        Iterable<FinanzdatenDto> ausgaben = service.findAllAusgabenByKontoId(kontoId);
        return ResponseEntity.ok(ausgaben);
    }
}