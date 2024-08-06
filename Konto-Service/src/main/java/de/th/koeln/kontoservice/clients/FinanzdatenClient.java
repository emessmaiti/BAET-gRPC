package de.th.koeln.kontoservice.clients;

import de.th.koeln.finanzdaten.grpc.*;
import de.th.koeln.kontoservice.exceptions.NotFoundException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service-Klasse für die Kommunikation mit dem Finanzdaten-Service über gRPC.
 *
 * Diese Klasse stellt Methoden bereit, um Einnahmen und Ausgaben eines Kontos abzurufen,
 * sowie deren Summen zu berechnen. Die Kommunikation erfolgt über die gRPC-Schnittstelle.
 */
@Service
public class FinanzdatenClient {

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
     * für das gegebene Konto abzurufen. Wenn das Konto gefunden wird, werden die Einnahmen
     * zurückgegeben. Andernfalls wird eine NotFoundException ausgelöst.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Liste aller Einnahmen des Kontos.
     * @throws NotFoundException wenn das Konto mit der angegebenen ID nicht gefunden wird.
     */
    public List<FinanzdatenDTO> getAlleEinnahmen(Long kontoId) {
        GetAlleEinnahmenRequest einnahmenRequest = GetAlleEinnahmenRequest.newBuilder()
                .setKontoId(kontoId.toString())
                .build();
        try {
            GetAlleEinnahmenResponse einnahmeResponse = stub.getAlleEinnahmen(einnahmenRequest);
            return einnahmeResponse.getEinnahmenList();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden");

        }
    }

    /**
     * Ruft alle Ausgaben eines Kontos ab.
     *
     * Diese Methode sendet eine Anfrage an den Finanzdaten-Service, um alle Ausgaben
     * für das gegebene Konto abzurufen. Wenn das Konto gefunden wird, werden die Ausgaben
     * zurückgegeben. Andernfalls wird eine NotFoundException ausgelöst.
     *
     * @param kontoId Die ID des Kontos.
     * @return Eine Liste aller Ausgaben des Kontos.
     * @throws NotFoundException wenn das Konto mit der angegebenen ID nicht gefunden wird.
     */
    public List<FinanzdatenDTO> getAlleAusgaben(Long kontoId) {
        GetAlleAusgabenRequest ausgabenRequest = GetAlleAusgabenRequest.newBuilder()
                .setKontoId(kontoId.toString())
                .build();
        try {
            GetAlleAusgabenResponse ausgabeResponse = stub.getAlleAusgaben(ausgabenRequest);
            return ausgabeResponse.getAusgabenList();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden");

        }
    }

    /**
     * Ruft die Summe der Einnahmen eines Kontos ab.
     *
     * Diese Methode sendet eine Anfrage an den Finanzdaten-Service, um die Summe der Einnahmen
     * für das gegebene Konto abzurufen. Wenn das Konto gefunden wird, wird die Summe
     * zurückgegeben. Andernfalls wird eine NotFoundException ausgelöst.
     *
     * @param kontoId Die ID des Kontos.
     * @return Die Summe der Einnahmen des Kontos.
     * @throws NotFoundException wenn das Konto mit der angegebenen ID nicht gefunden wird.
     */
    public BigDecimal getEinnahmenSumme (Long kontoId) {
        GetEinnahmenSummeRequest einnahmenRequest = GetEinnahmenSummeRequest.newBuilder()
                .setKontoId(kontoId.toString())
                .build();
        try {
            GetEinnahmenSummeResponse einnahmenResponse = stub.getEinnahmenSumme(einnahmenRequest);
            return BigDecimal.valueOf(einnahmenResponse.getEinnahmenSumme());
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden");

        }
    }

    /**
     * Ruft die Summe der Ausgaben eines Kontos ab.
     *
     * Diese Methode sendet eine Anfrage an den Finanzdaten-Service, um die Summe der Ausgaben
     * für das gegebene Konto abzurufen. Wenn das Konto gefunden wird, wird die Summe
     * zurückgegeben. Andernfalls wird eine NotFoundException ausgelöst.
     *
     * @param kontoId Die ID des Kontos.
     * @return Die Summe der Ausgaben des Kontos.
     * @throws NotFoundException wenn das Konto mit der angegebenen ID nicht gefunden wird.
     */
    public BigDecimal getAusgabenSumme (Long kontoId) {
        GetAusgabenSummeRequest ausgabenRequest = GetAusgabenSummeRequest.newBuilder()
                .setKontoId(kontoId.toString())
                .build();
        try {
            GetAusgabenSummeResponse einnahmenResponse = stub.getAusgabenSumme(ausgabenRequest);
            return BigDecimal.valueOf(einnahmenResponse.getAusgabensumme());
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden");

        }
    }
}
