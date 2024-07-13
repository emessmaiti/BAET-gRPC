package de.th.koeln.benutzerdatenservice.controllers;

import de.th.koeln.benutzerdatenservice.entities.Benutzerdaten;
import de.th.koeln.benutzerdatenservice.services.BenutzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Der BenutzerdatenController stellt REST-Endpunkte zur Verwaltung von Benutzerdaten bereit.
 *
 * <p>Diese Klasse verwendet {@link BenutzerService}, um die Geschäftslogik für Benutzerdatenoperationen
 * auszuführen. Zu den verfügbaren Endpunkten gehören das Erstellen, Aktualisieren, Löschen und Abrufen
 * von Benutzerdaten.</p>
 */
@RestController
@RequestMapping("/api/benutzer")
public class BenutzerController {

    private final BenutzerService service;

    @Autowired
    public BenutzerController(BenutzerService service) {
        this.service = service;
    }

    /**
     * Erstellt einen neuen Benutzer.
     *
     * @param benutzerdaten Die Benutzerdaten des zu erstellenden Benutzers.
     * @return Die erstellten Benutzerdaten.
     */
    @PostMapping
    public Benutzerdaten createBenutzer(@RequestBody Benutzerdaten benutzerdaten) {
        return service.speichern(benutzerdaten);
    }

    /**
     * Aktualisiert die Benutzerdaten des angegebenen Benutzers.
     *
     * @param id       Die ID des zu aktualisierenden Benutzers.
     * @param benutzer Die neuen Benutzerdaten.
     * @return Die aktualisierten Benutzerdaten.
     */
    @PutMapping("/{id}")
    public Benutzerdaten updateBenutzer(@PathVariable("id") long id, @RequestBody Benutzerdaten benutzer) {
        benutzer.setId(id);
        return service.updateBenutzer(benutzer);
    }

    /**
     * Löscht den Benutzer mit der angegebenen ID.
     *
     * @param id Die ID des zu löschenden Benutzers.
     * @return Eine leere {@link ResponseEntity}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBenutzer(@PathVariable("id") long id) {
        service.loeschen(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Findet einen Benutzer anhand der Sub (Subject-ID).
     *
     * @param sub Die Subject-ID des Benutzers.
     * @return Die Benutzerdaten des gefundenen Benutzers.
     */
    @GetMapping("/sub/{sub}")
    public Benutzerdaten getBenutzerBySub(@PathVariable("sub") String sub) {
        return service.findBenutzerBySub(sub);
    }

    /**
     * Findet einen Benutzer anhand der ID.
     *
     * @param id Die ID des Benutzers.
     * @return Die Benutzerdaten des gefundenen Benutzers.
     */
    @GetMapping("/id/{id}")
    public Benutzerdaten getBenutzerById(@PathVariable("id") long id) {
        return service.findBenutzerById(id);
    }

    /**
     * Aktualisiert das Datum und die Uhrzeit der letzten Anmeldung eines Benutzers.
     *
     * @param sub Die Subject-ID des Benutzers.
     */
    @PutMapping("/lastLogin/{sub}")
    public void updateLetzteAnmeldung(@PathVariable String sub) {
        service.updateLastLogin(sub);
    }

}
