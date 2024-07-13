package de.th.koeln.finanzdatenservice.controllers;

import de.th.koeln.finanzdatenservice.entities.FinanzielleZiel;
import de.th.koeln.finanzdatenservice.services.BaseService;
import de.th.koeln.finanzdatenservice.services.FinanzielleZielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-Controller zur Verwaltung von finanziellen Zielen.
 *
 * <p>Dieser Controller erweitert {@link BaseController} und bietet Endpunkte zur Verwaltung von finanziellen Zielen.</p>
 */
@RestController
@RequestMapping("/api/ziel")
public class FinanzielleZielController extends BaseController<FinanzielleZiel> {

    /**
     * Konstruktor zur Initialisierung des BaseService.
     *
     * @param service Der Service zur Verwaltung der finanziellen Ziele.
     */
    @Autowired
    protected FinanzielleZielController(BaseService<FinanzielleZiel> service) {
        super(service);
    }




}
