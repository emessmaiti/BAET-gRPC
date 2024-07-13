package de.th.koeln.kontoservice.services;

import de.th.koeln.kontoservice.entities.Kontodaten;
import de.th.koeln.kontoservice.exceptions.NotFoundException;
import de.th.koeln.kontoservice.repositories.KontodatenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service zur Verwaltung von Kontodaten.
 *
 * <p>Dieser Service bietet Methoden zur Erstellung, Aktualisierung, Löschung und Abfrage von Kontodaten.
 * Er kommuniziert mit externen Services über Feign Clients, um Benutzerdaten, Einnahmen und Ausgaben zu verwalten.</p>
 */
@Service
@Transactional
public class KontodatenService {

    private final KontodatenRepository repository;

    @Autowired
    public KontodatenService(KontodatenRepository repository) {
        this.repository = repository;
    }

    /**
     * Speichert Kontodaten.
     *
     * @param kontodaten Die zu speichernden Kontodaten.
     * @return Die gespeicherten Kontodaten.
     */
    public Kontodaten save(Kontodaten kontodaten) {
        return this.repository.save(kontodaten);
    }

    /**
     * Aktualisiert Kontodaten.
     *
     * @param id Die ID der zu aktualisierenden Kontodaten.
     * @param kontodaten Die aktualisierten Kontodaten.
     * @return Die aktualisierten Kontodaten.
     */
    public Kontodaten update(Long id, Kontodaten kontodaten) {
        Optional<Kontodaten> kd = repository.findById(id);
        if (kd.isPresent()) {
            Kontodaten existingKD = kd.get();
            existingKD.setKontostand(kontodaten.getKontostand());
            return repository.save(existingKD);
        } else {
            throw new RuntimeException("Kontodaten mit der ID " + id + " existiert nicht");
        }
    }

    /**
     * Löscht Kontodaten.
     *
     * @param id Die ID der zu löschenden Kontodaten.
     */
    public void delete(Long id) {
        Optional<Kontodaten> kd = findKontoById(id);
        kd.ifPresent(repository::delete);
    }

    /**
     * Findet Kontodaten anhand der ID.
     *
     * @param id Die ID der Kontodaten.
     * @return Die gefundenen Kontodaten.
     */
    public Optional<Kontodaten> findKontoById(Long id) {
       Optional<Kontodaten> kd = repository.findById(id);
        if (kd.isPresent()) {
            return kd;
        }
        throw new NotFoundException("Konto mit der ID:" + id + " konnte nicht gefunden");
    }

    /**
     * Findet Kontodaten anhand der Benutzer-ID.
     *
     * @param benutzerId Die Benutzer-ID.
     * @return Die gefundenen Kontodaten.
     */
    public Optional<Kontodaten> findByBenutzerId(String benutzerId) {
        return Optional.ofNullable(this.repository.findKontodatenByBenutzerId(benutzerId));
    }


}
