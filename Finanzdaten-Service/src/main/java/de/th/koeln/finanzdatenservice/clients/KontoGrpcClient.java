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

    @GrpcClient("konto-service")
    private KontoServiceGrpc.KontoServiceBlockingStub stub;

    public KontoDaten findKontoById(String id) {
        FindKontoByIdRequest request = FindKontoByIdRequest.newBuilder()
                .setKontoId(id)
                .build();
        try {
            FindKontoByIdResponse response = stub.findKontoById(request);
            return response.getKonto();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + id + " konnte nicht gefunden werden");

        }
    }
}
