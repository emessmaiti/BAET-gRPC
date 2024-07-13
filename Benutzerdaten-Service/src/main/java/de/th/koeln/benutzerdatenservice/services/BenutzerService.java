package de.th.koeln.benutzerdatenservice.services;

import de.th.koeln.benutzerdatenservice.entities.Benutzerdaten;
import de.th.koeln.benutzerdatenservice.exceptions.BenutzerdatenExceptionHandler;
import de.th.koeln.benutzerdatenservice.repositories.BenutzerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BenutzerService {

    private final BenutzerRepository repository;

    @Autowired
    public BenutzerService(BenutzerRepository repository) {
        this.repository = repository;
    }

    /**
     * Speichert die angegebenen Benutzerdaten.
     *
     * @param benutzer Die zu speichernden Benutzerdaten.
     * @return Die gespeicherten Benutzerdaten.
     */
    @Transactional
    public Benutzerdaten speichern(Benutzerdaten benutzer) {
        return repository.save(benutzer);
    }

    /**
     * Löscht die Benutzerdaten mit der angegebenen ID.
     *
     * @param id Die ID der zu löschenden Benutzerdaten.
     */
    @Transactional
    public void loeschen(long id) {
        findBenutzerById(id);
        repository.deleteById(id);
    }

    /**
     * Findet einen Benutzer anhand der ID.
     *
     * @param Id Die ID des Benutzers.
     * @return Die Benutzerdaten des Benutzers.
     */
    public Benutzerdaten findBenutzerById(Long Id) {
        Optional<Benutzerdaten> benutzer = repository.findById(Id);
        return benutzer.orElseThrow(() -> new BenutzerdatenExceptionHandler("Benutzer mit der Id " + Id + " existiert nicht."));
    }

    /**
     * Aktualisiert die angegebenen Benutzerdaten.
     *
     * @param benutzer Die zu aktualisierenden Benutzerdaten.
     * @return Die aktualisierten Benutzerdaten.
     */
    @Transactional
    public Benutzerdaten updateBenutzer(Benutzerdaten benutzer) {
        Benutzerdaten existingBenutzer = findBenutzerById(benutzer.getId());
        existingBenutzer.setVorname(benutzer.getVorname());
        existingBenutzer.setNachname(benutzer.getNachname());
        existingBenutzer.setEmail(benutzer.getEmail());
        existingBenutzer.setGeschlecht(benutzer.getGeschlecht());

        return repository.save(existingBenutzer);

    }

    /**
     * Findet einen Benutzer anhand der Sub (Subject-ID).
     *
     * @param sub Die Subject-ID des Benutzers.
     * @return Die Benutzerdaten des Benutzers.
     */
    public Benutzerdaten findBenutzerBySub(String sub) {
        return repository.findBenutzerdatenBySub(sub);
    }

    /**
     * Aktualisiert das Datum und die Uhrzeit der letzten Anmeldung eines Benutzers anhand der Sub (Subject-ID).
     *
     * @param sub Die Subject-ID des Benutzers.
     */
    @Transactional
    public void updateLastLogin(String sub) {
        repository.updateLastLogin(sub, LocalDateTime.now());
    }



}
