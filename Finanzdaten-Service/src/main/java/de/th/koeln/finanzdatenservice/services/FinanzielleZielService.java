package de.th.koeln.finanzdatenservice.services;

import de.th.koeln.benutzerservice.grpc.BenutzerDaten;
import de.th.koeln.finanzdatenservice.clients.BenutzerGrpcClient;
import de.th.koeln.finanzdatenservice.entities.FinanzielleZiel;
import de.th.koeln.finanzdatenservice.repositories.BaseRepository;
import de.th.koeln.finanzdatenservice.repositories.FinanzielleZielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Der Service für die Verwaltung von finanziellen Zielen.
 *
 * <p>Dieser Service erweitert {@link BaseService} und bietet allgemeine Methoden zur Verwaltung von finanziellen Zielen.</p>
 */
@Service
public class FinanzielleZielService extends BaseService<FinanzielleZiel> {

    private final BenutzerGrpcClient benutzerClient;

    /**
     * Konstruktor zur Initialisierung des Repositories.
     *
     * @param repository Das Repository zur Verwaltung der finanziellen Ziele.
     */
    @Autowired
    protected FinanzielleZielService(BaseRepository<FinanzielleZiel> repository, BenutzerGrpcClient benutzerClient) {
        super(repository);
        this.benutzerClient = benutzerClient;
    }

    public BenutzerDaten findBenutzerById(String id) {
        return benutzerClient.findBenutzerById(id);
    }


}
