package de.th.koeln.finanzdatenservice.clients;

import de.th.koeln.benutzerservice.grpc.BenutzerDaten;
import de.th.koeln.benutzerservice.grpc.BenutzerServiceGrpc;
import de.th.koeln.benutzerservice.grpc.GetBenutzerBySubRequest;
import de.th.koeln.benutzerservice.grpc.GetBenutzerBySubResponse;
import de.th.koeln.finanzdatenservice.exceptions.NotFoundException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * Service-Klasse für die Kommunikation mit dem Benutzer-Service über gRPC.
 *
 * Diese Klasse stellt Methoden bereit, um Benutzerdaten vom Benutzer-Service
 * abzurufen. Die Kommunikation erfolgt über die gRPC-Schnittstelle.
 */
@Service
public class BenutzerGrpcClient {

    /**
     * Der gRPC-Client Stub für die Kommunikation mit dem Benutzer-Service.
     *
     * Dieser Stub wird verwendet, um synchrone Aufrufe an den Benutzer-Service durchzuführen.
     * Die Annotation @GrpcClient weist Spring Boot an, diesen Stub mit den entsprechenden
     * Konfigurationsdetails zu initialisieren, die unter dem Namen "benutzer-service" definiert sind.
     */
    @GrpcClient("benutzer-service")
    private BenutzerServiceGrpc.BenutzerServiceBlockingStub stub;

    /**
     * Findet Benutzerdaten basierend auf der Benutzer-ID (Sub).
     *
     * Diese Methode sendet eine Anfrage an den Benutzer-Service, um die Benutzerdaten
     * für eine gegebene Sub (ein eindeutiger Identifikator) abzurufen. Wenn der Benutzer
     * gefunden wird, werden die Benutzerdaten zurückgegeben. Andernfalls wird eine
     * NotFoundException ausgelöst.
     *
     * @param id Die Sub des Benutzers.
     * @return Die Benutzerdaten.
     * @throws NotFoundException wenn der Benutzer nicht gefunden wird.
     */
    public BenutzerDaten findBenutzerById(String id) {
        GetBenutzerBySubRequest request = GetBenutzerBySubRequest.newBuilder()
                .setSub(id)
                .build();
        try {
            // Senden der Anfrage und Empfangen der Antwort
            GetBenutzerBySubResponse response = stub.getBenutzerBySub(request);
            return response.getBenutzer();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Benutzer not found");
        }
    }

}
