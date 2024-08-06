package de.th.koeln.benachrichtigungservice.controllers;

import de.th.koeln.benachrichtigungservice.services.BenachrichtigungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller-Klasse für Benachrichtigungen.
 *
 * Diese Klasse stellt Endpunkte bereit, um Benachrichtigungen zu versenden.
 */
@RestController
@RequestMapping("/api/notification")
public class BenachrichtigungController {

    private final BenachrichtigungService benachrichtigungService;

    /**
     * Konstruktor für BenachrichtigungController.
     *
     * @param benachrichtigungService Der Dienst zur Verwaltung von Benachrichtigungen.
     */
    @Autowired
    public BenachrichtigungController(BenachrichtigungService benachrichtigungService) {
        this.benachrichtigungService = benachrichtigungService;
    }

    /**
     * Sendet eine Benachrichtigung.
     *
     * Dieser Endpunkt löst das Senden einer Benachrichtigung über den BenachrichtigungService aus.
     *
     * @return Eine ResponseEntity mit der Bestätigung, dass die Benachrichtigung erfolgreich versendet wurde.
     */
    @GetMapping("/notify")
    public ResponseEntity<String> versendeBenachrichtigung(){
        benachrichtigungService.benachrichtige();
        return ResponseEntity.ok("Benachrichtigung wurde erfolgreich versendet");
    }

}
