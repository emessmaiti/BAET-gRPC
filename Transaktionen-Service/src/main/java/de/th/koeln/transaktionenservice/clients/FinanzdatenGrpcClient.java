package de.th.koeln.transaktionenservice.clients;

import de.th.koeln.finanzdaten.grpc.*;
import de.th.koeln.transaktionenservice.exceptions.NotFoundException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-Klasse für die Kommunikation mit dem Finanzdaten-Service über gRPC.
 *
 * Diese Klasse stellt Methoden bereit, um Einnahmen und Ausgaben eines Kontos abzurufen.
 * Die Kommunikation erfolgt über die gRPC-Schnittstelle.
 */
@Service
public class FinanzdatenGrpcClient {

    /**
     * Der gRPC-Client Stub für die Kommunikation mit dem Finanzdaten-Service.
     *
     * Dieser Stub wird verwendet, um synchrone Aufrufe an den Finanzdaten-Service durchzuführen.
     * Die Annotation @GrpcClient weist Spring Boot an, diesen Stub mit den entsprechenden
     * Konfigurationsdetails zu initialisieren, die unter dem Namen "finanzdaten-service" definiert sind.
     */
    @GrpcClient("finanzdaten-service")
    private FinanzdatenServiceGrpc.FinanzdatenServiceBlockingStub stub;

    /**
     * Ruft alle Einnahmen eines Kontos ab.
     *
     * Diese Methode sendet eine Anfrage an den Finanzdaten-Service, um alle Einnahmen
     * des aktuellen Monats für das gegebene Konto abzurufen. Wenn das Konto gefunden wird,
     * werden die Einnahmen zurückgegeben. Andernfalls wird eine NotFoundException ausgelöst.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Liste aller Einnahmen des Kontos.
     * @throws NotFoundException wenn das Konto mit der angegebenen ID nicht gefunden wird.
     */
    public List<FinanzdatenDTO> getAllEinnahmen(Long kontoId) {
        GetEinnahmenDesMonatsRequest einnahmenRequest = GetEinnahmenDesMonatsRequest.newBuilder()
                .setKontoId(kontoId.toString())
                .build();
        try {
            GetEinnahmenDesMonatsResponse einnahmeResponse = stub.getEinnahmenDesMonats(einnahmenRequest);
            return einnahmeResponse.getEinnahmenList();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden");
        }
    }

    /**
     * Ruft alle Ausgaben eines Kontos ab.
     *
     * Diese Methode sendet eine Anfrage an den Finanzdaten-Service, um alle Ausgaben
     * des aktuellen Monats für das gegebene Konto abzurufen. Wenn das Konto gefunden wird,
     * werden die Ausgaben zurückgegeben. Andernfalls wird eine NotFoundException ausgelöst.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Liste aller Ausgaben des Kontos.
     * @throws NotFoundException wenn das Konto mit der angegebenen ID nicht gefunden wird.
     */
    public List<FinanzdatenDTO> getAllAusgaben(Long kontoId) {
        GetAusgabenDesMonatsRequest ausgabenRequest = GetAusgabenDesMonatsRequest.newBuilder()
                .setKontoId(kontoId.toString())
                .build();
        try {
            GetAusgabenDesMonatsResponse ausgabeResponse = stub.getAusgabenDesMonats(ausgabenRequest);
            return ausgabeResponse.getAusgabenList();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden");
        }
    }
}
