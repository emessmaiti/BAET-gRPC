package de.th.koeln.benachrichtigungservice.controllers;

import de.th.koeln.benachrichtigungservice.services.BenachrichtigungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class BenachrichtigungController {

    private final BenachrichtigungService benachrichtigungService;

    @Autowired
    public BenachrichtigungController(BenachrichtigungService benachrichtigungService) {
        this.benachrichtigungService = benachrichtigungService;
    }

    @GetMapping("/notify")
    public ResponseEntity<String> versendeBenachrichtigung(){
        benachrichtigungService.benachrichtige();
        return ResponseEntity.ok("Benachrichtigung wurde erfolgreich versendet");
    }

}
