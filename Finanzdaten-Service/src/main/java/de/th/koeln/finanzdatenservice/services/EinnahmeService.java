package de.th.koeln.finanzdatenservice.services;

import de.th.koeln.finanzdatenservice.clients.KontoGrpcClient;
import de.th.koeln.finanzdatenservice.entities.Einnahme;
import de.th.koeln.finanzdatenservice.entities.EinnahmeKategorie;
import de.th.koeln.finanzdatenservice.exceptions.NotFoundException;
import de.th.koeln.finanzdatenservice.repositories.BaseRepository;
import de.th.koeln.finanzdatenservice.repositories.EinnahmeRepository;
import de.th.koeln.kontoservice.grpc.KontoDaten;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

/**
 * Der Service für die Verwaltung von Einnahmen.
 * <p>Dieser Service erweitert {@link BaseService} und bietet zusätzliche Methoden.</p>
 */
@Service
public class EinnahmeService extends BaseService<Einnahme> {

    protected EinnahmeRepository repository;
    protected KontoGrpcClient kontoClient;

    /**
     * Konstruktor zur Initialisierung des Repositories und des KontoClients.
     *
     * @param repository Das Repository zur Verwaltung der Einnahmen.
     * @param -kontoClient Der Client zur Interaktion mit dem Konto-Service.
     */
    @Autowired
    protected EinnahmeService(BaseRepository<Einnahme> repository, KontoGrpcClient kontoClient) {
        super(repository);
        this.repository = (EinnahmeRepository) repository;
        this.kontoClient = kontoClient;
    }

    /**
     * Holt die Einnahmen eines Benutzers für einen bestimmten Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @param monat Der Monat, für den die Einnahmen abgerufen werden sollen.
     * @return Eine Menge von Einnahmen des Benutzers für den angegebenen Monat.
     */
    public Set<Einnahme> holeEinnahmenBeiDatum(String benutzerId, int monat) {
        this.repository.findByBenutzerID(benutzerId);
        return this.repository.findEinnahmeByMonth(benutzerId, monat);
    }

    /**
     * Holt die Einnahmen eines Benutzers für den aktuellen Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Eine Menge von Einnahmen des Benutzers für den aktuellen Monat.
     */
    public Set<Einnahme> holeEinnahmenAktuellesDatum(String benutzerId) {
        return this.repository.findEinnahmeByMonth(benutzerId, LocalDate.now().getMonthValue());
    }

    /**
     * Holt die Einnahmen eines Kontos für den aktuellen Monat.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Menge von Einnahmen des Kontos für den aktuellen Monat.
     */
    public Set<Einnahme> holeEinnahmenAktuellesDatum(Long kontoId) {
        Optional<KontoDaten> kontoDTO = Optional.ofNullable(this.kontoClient.findKontoById(String.valueOf(kontoId)));
        if(kontoDTO.isPresent()){
            return this.repository.findEinnahmeByMonth(kontoId, LocalDate.now().getMonthValue());
        }
        throw new NotFoundException("Konto not found");
    }

    /**
     * Berechnet die Summe der Einnahmen eines Benutzers für den aktuellen Monat.
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Die Summe der Einnahmen des Benutzers für den aktuellen Monat.
     */
    public BigDecimal getSummeEinnahmenDesMonat(String benutzerId) {
        Set<Einnahme> einnahmeSet = holeEinnahmenAktuellesDatum(benutzerId);
        BigDecimal summeEinnahmen = BigDecimal.ZERO;
        for (Einnahme einnahme : einnahmeSet) {
            if (einnahme != null && einnahme.getBetrag() != null) {
                summeEinnahmen = summeEinnahmen.add(einnahme.getBetrag());
            }
        }
        return summeEinnahmen;
    }

    /**
     * Berechnet die Summe der Einnahmen eines Kontos für den aktuellen Monat.
     *
     * @param kontoId Die ID des Kontos.
     * @return Die Summe der Einnahmen des Kontos für den aktuellen Monat.
     */
    public BigDecimal getSummeEinnahmenDesMonat(Long kontoId) {
        Set<Einnahme> einnahmeSet = holeEinnahmenAktuellesDatum(kontoId);
        BigDecimal summeEinnahmen = BigDecimal.ZERO;
        for (Einnahme einnahme : einnahmeSet) {
            if (einnahme != null && einnahme.getBetrag() != null) {
                summeEinnahmen = summeEinnahmen.add(einnahme.getBetrag());
            }
        }
        return summeEinnahmen;
    }

    /**
     * Holt alle Einnahmen eines Benutzers, sortiert nach Datum absteigend.
     *
     * @param id Die ID des Benutzers.
     * @return Eine Menge von Einnahmen des Benutzers, sortiert nach Datum absteigend.
     */
    public Set<Einnahme> holleAlleEinnahmeDesc(String id) {
        findByBenutzerId(id);
        return this.repository.findAllOrderByDatumDesc(id);
    }

    /**
     * Findet eine Einnahme anhand der Kategorie.
     *
     * @param kategorie Die Einnahmekategorie.
     * @return Die Einnahme der angegebenen Kategorie.
     */
    public Einnahme findByKategorie(EinnahmeKategorie kategorie) {
        return this.repository.findEinnahmeByEinnahmeKategorie(kategorie);
    }
}
