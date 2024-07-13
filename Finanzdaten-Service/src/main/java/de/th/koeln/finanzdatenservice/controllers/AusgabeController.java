package de.th.koeln.finanzdatenservice.controllers;

import de.th.koeln.finanzdatenservice.entities.Ausgabe;
import de.th.koeln.finanzdatenservice.entities.AusgabeKategorie;
import de.th.koeln.finanzdatenservice.services.AusgabeService;
import de.th.koeln.finanzdatenservice.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * REST-Controller zur Verwaltung von Ausgaben.
 *
 * <p>Dieser Controller erweitert {@link BaseController} und bietet Endpunkte zur Verwaltung von Ausgaben,
 * einschließlich des Abrufens von Ausgaben nach Monat oder Kategorie und der Berechnung von Ausgabensummen.</p>
 */
@RestController
@RequestMapping("/api/ausgaben")
public class AusgabeController extends BaseController<Ausgabe> {

    @Autowired
    protected AusgabeService service;

    /**
     * Konstruktor zur Initialisierung des BaseService.
     *
     * @param baseService Der Service zur Verwaltung der Ausgaben.
     */
    protected AusgabeController(BaseService<Ausgabe> baseService) {
        super(baseService);
    }

    /**
     * Berechnet die Summe der Ausgaben eines Benutzers für den aktuellen Monat.
     *
     * @param jwt Der JWT-Token des authentifizierten Benutzers.
     * @return Die Summe der Ausgaben des Benutzers für den aktuellen Monat.
     */
    @GetMapping("/getSumme")
    public ResponseEntity<BigDecimal> getSumme(@AuthenticationPrincipal Jwt jwt) {
        String benutzerID = jwt.getSubject();
        BigDecimal summe = this.service.getSummeAlleAusgaben(benutzerID);
        return ResponseEntity.ok(summe);
    }

    /**
     * Holt alle Ausgaben eines Benutzers für den aktuellen Monat, sortiert nach Datum absteigend.
     *
     * @param benutzerID Die ID des Benutzers.
     * @return Eine Menge von Ausgaben des Benutzers für den aktuellen Monat, sortiert nach Datum absteigend.
     */
    @GetMapping("all/monat/{benutzerID}/desc")
    public Set<Ausgabe> getAlleAusgabenByMonatDesc(@PathVariable String benutzerID) {
        return this.service.holeAllAusgabenByDatumDesc(benutzerID);
    }

    /**
     * Holt alle Ausgaben eines Benutzers für den aktuellen Monat.
     *
     * @param jwt Der JWT-Token des authentifizierten Benutzers.
     * @return Eine Menge von Ausgaben des Benutzers für den aktuellen Monat.
     */
    @GetMapping("/all")
    public Set<Ausgabe> getAlleAusgabenAktuellesMonats(@AuthenticationPrincipal Jwt jwt) {
        String benutzerId = jwt.getSubject();
        return this.service.holeAusgabenAktuellesDatum(benutzerId);
    }

    /**
     * Holt alle Ausgaben eines Kontos für den aktuellen Monat.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Menge von Ausgaben des Kontos für den aktuellen Monat.
     */
    @GetMapping("/all/monat/{kontoId}")
    public Set<Ausgabe> getAlleAusgabenAktuellesMonats(@PathVariable Long kontoId) {
        return this.service.holeAusgabenAktuellesDatum(kontoId);
    }

    /**
     * Holt eine Ausgabe anhand der Kategorie.
     *
     * @param kategorie Die Ausgabenkategorie.
     * @return Die Ausgabe der angegebenen Kategorie.
     */
    @GetMapping("/kategorie")
    public Ausgabe getKategorie(@RequestParam AusgabeKategorie kategorie) {
        return this.service.findByKategorie(kategorie);
    }

    /**
     * Berechnet die Summe der Ausgaben eines Benutzers für den aktuellen Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Die Summe der Ausgaben des Benutzers für den aktuellen Monat.
     */
    @GetMapping("/getSumme/benutzer/{benutzerId}")
    BigDecimal getAusgabenSumme(@PathVariable String benutzerId) {
        return this.service.getSummeAusgabenDesMonat(benutzerId);
    }

    /**
     * Berechnet die Summe der Ausgaben eines Kontos für den aktuellen Monat.
     *
     * @param kontoId Die ID des Kontos.
     * @return Die Summe der Ausgaben des Kontos für den aktuellen Monat.
     */
    @GetMapping("/getSumme/konto/{kontoId}")
    BigDecimal getAusgabenSumme(@PathVariable Long kontoId) {
        return this.service.getSummeAusgabenDesMonat(kontoId);
    }
}
