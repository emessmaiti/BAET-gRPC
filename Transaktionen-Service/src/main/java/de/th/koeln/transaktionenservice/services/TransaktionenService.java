package de.th.koeln.transaktionenservice.services;

import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;
import de.th.koeln.transaktionenservice.clients.FinanzdatenGrpcClient;
import de.th.koeln.transaktionenservice.dto.FinanzdatenDto;
import de.th.koeln.transaktionenservice.dto.FinanzdatenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service-Klasse für Transaktionen.
 *
 * Diese Klasse verwaltet die Logik zum Abrufen und Verarbeiten von Finanzdaten,
 * einschließlich der Einnahmen und Ausgaben eines Kontos.
 */
@Service
public class TransaktionenService {

    private final FinanzdatenGrpcClient client;

    /**
     * Konstruktor für TransaktionenService.
     *
     * @param client Der gRPC-Client für Finanzdaten.
     */
    @Autowired
    public TransaktionenService(FinanzdatenGrpcClient client) {
        this.client = client;
    }

    /**
     * Ruft alle Finanzdaten eines Kontos ab.
     *
     * Diese Methode ruft alle Einnahmen und Ausgaben für das gegebene Konto ab,
     * konvertiert sie in DTOs, sortiert sie nach Datum und gibt eine Liste der
     * kombinierten und sortierten Finanzdaten zurück.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine sortierte Liste aller Finanzdaten des Kontos.
     */
    public List<FinanzdatenDto> getAlleFinanzdaten(Long kontoId) {
        // Ruft alle Einnahmen und Ausgaben für das Konto ab
        List<FinanzdatenDTO> einnahmenProto = this.client.getAllEinnahmen(kontoId);
        List<FinanzdatenDTO> ausgabenProto = this.client.getAllAusgaben(kontoId);

        // Konvertiert die Protobuf-Objekte in DTOs
        List<FinanzdatenDto> einnahmen = einnahmenProto.stream()
                .map(FinanzdatenMapper::toDto)
                .toList();

        List<FinanzdatenDto> ausgaben = ausgabenProto.stream()
                .map(FinanzdatenMapper::toDto)
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Kombiniert und sortiert die Einnahmen und Ausgaben nach Datum absteigend
        return Stream.concat(einnahmen.stream(), ausgaben.stream())
                .sorted(Comparator.comparing((FinanzdatenDto f) -> LocalDate.parse(f.getDatum(), formatter)).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
