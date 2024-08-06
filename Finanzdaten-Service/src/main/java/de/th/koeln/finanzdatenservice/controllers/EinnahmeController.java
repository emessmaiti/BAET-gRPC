package de.th.koeln.finanzdatenservice.controllers;

import de.th.koeln.finanzdatenservice.entities.Einnahme;
import de.th.koeln.finanzdatenservice.entities.EinnahmeKategorie;
import de.th.koeln.finanzdatenservice.services.BaseService;
import de.th.koeln.finanzdatenservice.services.EinnahmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * REST-Controller zur Verwaltung von Einnahmen.
 *
 * <p>Dieser Controller erweitert {@link BaseController} und bietet Endpunkte zur Verwaltung von Einnahmen,
 * einschließlich des Abrufens von Einnahmen nach Monat oder Kategorie und der Berechnung von Einnahmesummen.</p>
 */
@RestController
@RequestMapping("/api/einnahmen")
public class EinnahmeController extends BaseController<Einnahme> {

    @Autowired
    protected EinnahmeService service;

    /**
     * Konstruktor zur Initialisierung des BaseService.
     *
     * @param baseService Der Service zur Verwaltung der Einnahmen.
     */
    protected EinnahmeController(BaseService<Einnahme> baseService) {
        super(baseService);
    }

    /**
     * Holt alle Einnahmen eines Benutzers, sortiert nach Datum absteigend.
     *
     * @param id Die ID des Benutzers.
     * @return Eine Menge von Einnahmen des Benutzers, sortiert nach Datum absteigend.
     */
    @GetMapping("/all/desc/{id}")
    public Set<Einnahme> getAlleEinnahmenDesc(@PathVariable String id) {
        return this.service.holleAlleEinnahmeDesc(id);
    }

    /**
     * Holt alle Einnahmen eines Benutzers für einen bestimmten Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @param monat Der Monat, für den die Einnahmen abgerufen werden sollen.
     * @return Eine Menge von Einnahmen des Benutzers für den angegebenen Monat.
     */
    @GetMapping("/all/monat/{benutzerId}/{monat}")
    public Set<Einnahme> getAlleEinnahmenAktuellesMonats(@PathVariable String benutzerId, @PathVariable int monat) {
        return this.service.holeEinnahmenBeiDatum(benutzerId, monat);
    }

    /**
     * Holt alle Einnahmen eines Benutzers für den aktuellen Monat.
     *
     * @param jwt Der JWT-Token des authentifizierten Benutzers.
     * @return Eine Menge von Einnahmen des Benutzers für den aktuellen Monat.
     */
    @GetMapping("/all")
    public Set<Einnahme> getAlleEinnahmenAktuellesMonats(@AuthenticationPrincipal Jwt jwt) {
        String benutzerId = jwt.getSubject();
        return this.service.holeEinnahmenAktuellesDatum(benutzerId);
    }

    /**
     * Holt alle Einnahmen eines Kontos für den aktuellen Monat.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Menge von Einnahmen des Kontos für den aktuellen Monat.
     */
    @GetMapping("/all/monat/{kontoId}")
    public Set<Einnahme> getAlleEinnahmenAktuellesMonats(@PathVariable Long kontoId) {
        return this.service.holeEinnahmenAktuellesDatum(kontoId);
    }

    /**
     * Berechnet die Summe der Einnahmen eines Benutzers für den aktuellen Monat.
     *
     * @param jwt Der JWT-Token des authentifizierten Benutzers.
     * @return Die Summe der Einnahmen des Benutzers für den aktuellen Monat.
     */
    @GetMapping("/getSumme")
    public ResponseEntity<BigDecimal> getSumme(@AuthenticationPrincipal Jwt jwt) {
        String benutzerID = jwt.getSubject();
        BigDecimal summe = this.service.getSummeEinnahmenDesMonat(benutzerID);
        return ResponseEntity.ok(summe);
    }

    /**
     * Holt eine Einnahme anhand der Kategorie.
     *
     * @param kategorie Die Einnahmekategorie.
     * @return Die Einnahme der angegebenen Kategorie.
     */
    @GetMapping("/kategorie")
    public Einnahme getKategorie(@RequestParam EinnahmeKategorie kategorie) {
        return this.service.findByKategorie(kategorie);
    }

    /**
     * Berechnet die Summe der Einnahmen eines Benutzers für den aktuellen Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Die Summe der Einnahmen des Benutzers für den aktuellen Monat.
     */
    @GetMapping("/getSumme/benutzer/{benutzerId}")
    BigDecimal getEinnahmeSumme(@PathVariable String benutzerId) {
        return this.service.getSummeEinnahmenDesMonat(benutzerId);
    }

    /**
     * Berechnet die Summe der Einnahmen eines Kontos für den aktuellen Monat.
     *
     * @param kontoId Die ID des Kontos.
     * @return Die Summe der Einnahmen des Kontos für den aktuellen Monat.
     */
    @GetMapping("/getSumme/konto/{kontoId}")
    BigDecimal getEinnahmeSumme(@PathVariable Long kontoId) {
        return this.service.getSummeEinnahmenDesMonat(kontoId);
    }
}
