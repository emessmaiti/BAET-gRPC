package de.th.koeln.finanzdatenservice.clients;

import de.th.koeln.finanzdatenservice.exceptions.NotFoundException;
import de.th.koeln.kontoservice.grpc.FindKontoByIdRequest;
import de.th.koeln.kontoservice.grpc.FindKontoByIdResponse;
import de.th.koeln.kontoservice.grpc.KontoDaten;
import de.th.koeln.kontoservice.grpc.KontoServiceGrpc;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * gRPC-Client für den Konto-Service.
 * Diese Klasse ermöglicht die Kommunikation mit dem Konto-Service über gRPC,
 * um Kontodaten abzurufen.
 *
 * <p>Der gRPC-Client wird mit der Adresse "localhost" und dem Port 9091 konfiguriert.
 * Die Kommunikation erfolgt ohne Verschlüsselung (plaintext).</p>
 *
 */

@Service
public class KontoGrpcClient  {

    /**
     * Der gRPC-Client Stub für die Kommunikation mit dem Konto-Service.
     *
     * Dieser Stub wird verwendet, um synchrone Aufrufe an den Konto-Service durchzuführen.
     * Die Annotation @GrpcClient weist Spring Boot an, diesen Stub mit den entsprechenden
     * Konfigurationsdetails zu initialisieren, die unter dem Namen "konto-service" definiert sind.
     */
    @GrpcClient("konto-service")
    private KontoServiceGrpc.KontoServiceBlockingStub stub;

    /**
     * Findet Kontodaten basierend auf der Konto-ID.
     *
     * Diese Methode sendet eine Anfrage an den Konto-Service, um die Kontodaten
     * für eine gegebene Konto-ID abzurufen. Wenn das Konto gefunden wird, werden
     * die Kontodaten zurückgegeben. Andernfalls wird eine NotFoundException ausgelöst.
     *
     * @param id Die ID des Kontos.
     * @return Die Kontodaten.
     * @throws NotFoundException wenn das Konto nicht gefunden wird.
     */
    public KontoDaten findKontoById(String id) {
        FindKontoByIdRequest request = FindKontoByIdRequest.newBuilder()
                .setKontoId(id)
                .build();
        try {
            // Senden der Anfrage und Empfangen der Antwort
            FindKontoByIdResponse response = stub.findKontoById(request);
            return response.getKonto();
        } catch (StatusRuntimeException e) {
            throw new StatusRuntimeException(e.getStatus());

        }
    }
}
