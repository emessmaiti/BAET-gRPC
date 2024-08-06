package de.th.koeln.kontoservice.services;

import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;
import de.th.koeln.kontoservice.clients.FinanzdatenClient;
import de.th.koeln.kontoservice.dto.FinanzdatenDto;
import de.th.koeln.kontoservice.dto.FinanzdatenMapper;
import de.th.koeln.kontoservice.entities.Kontodaten;
import de.th.koeln.kontoservice.exceptions.NotFoundException;
import de.th.koeln.kontoservice.repositories.KontodatenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final FinanzdatenClient client;

    @Autowired
    public KontodatenService(KontodatenRepository repository, FinanzdatenClient client) {
        this.repository = repository;
        this.client = client;
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

    /**
     * Berechnet den Kontostand anhand der Konto-ID.
     *
     * @param kontoId Die Konto-ID.
     * @return Der berechnete Kontostand.
     */
    public BigDecimal getKontoStandByKontoId(Long kontoId) {
        Optional<Kontodaten> kontoOpt = findKontoById(kontoId);

        if (kontoOpt.isEmpty()) {
            throw new NotFoundException("Konto nicht gefunden: " + kontoId);
        }
        Kontodaten kontodaten = kontoOpt.get();
        BigDecimal einnahmeSumme = this.client.getEinnahmenSumme(kontoId);
        BigDecimal ausgabeSumme = this.client.getAusgabenSumme(kontoId);
        BigDecimal kontostand = einnahmeSumme.subtract(ausgabeSumme);
        kontodaten.setKontostand(kontostand);
        this.repository.save(kontodaten);
        return kontostand;
    }

    /**
     * Holt alle Einnahmen eines Kontos anhand der Konto-ID.
     *
     * @param kontoId Die Konto-ID.
     * @return Eine Menge von Einnahmen-Daten-Transfer-Objekten (EinnahmeDTO) des Kontos.
     */
    public List<FinanzdatenDto> findAllEinnahmenByKontoId(Long kontoId) {
        List<FinanzdatenDTO> einnahmenProto = this.client.getAlleEinnahmen(kontoId);

        return  einnahmenProto.stream()
                .map(FinanzdatenMapper::toDto)
                .toList();
    }

    /**
     * Holt alle Ausgaben eines Kontos anhand der Konto-ID.
     *
     * @param kontoId Die Konto-ID.
     * @return Eine Iterable von Ausgaben-Daten-Transfer-Objekten (AusgabenDTO) des Kontos.
     */
    public Iterable<FinanzdatenDto> findAllAusgabenByKontoId(Long kontoId) {
        List<FinanzdatenDTO> ausgabenProto = this.client.getAlleAusgaben(kontoId);

        return ausgabenProto.stream()
                .map(FinanzdatenMapper::toDto)
                .toList();
    }

}
