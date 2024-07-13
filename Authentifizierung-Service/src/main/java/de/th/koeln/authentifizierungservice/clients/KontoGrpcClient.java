package de.th.koeln.authentifizierungservice.clients;

import de.th.koeln.kontoservice.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * gRPC-Client für den Konto-Service.
 * Diese Klasse ermöglicht die Kommunikation mit dem Konto-Service über gRPC,
 * um Kontodaten zu erstellen und abzurufen.
 *
 * <p>Der gRPC-Client wird mit der Adresse "localhost" und dem Port 9091 konfiguriert.
 * Die Kommunikation erfolgt ohne Verschlüsselung (plaintext).</p>
 *
 * <b>Beispiel:</b>
 * <pre>
 * KontoGrpcClient client = new KontoGrpcClient();
 * KontoDaten konto = client.createKonto(new KontoDaten(...));
 * </pre>
 */
@Service
public class KontoGrpcClient {

    @GrpcClient("konto-service")
    private KontoServiceGrpc.KontoServiceBlockingStub stub;

    /**
     * Erstellt ein neues Konto.
     * Diese Methode sendet einen CreateKontoRequest an den Konto-Service,
     * um die angegebenen Kontodaten zu speichern.
     *
     * @param konto Die zu speichernden Kontodaten.
     * @return Die gespeicherten Kontodaten.
     */
    public KontoDaten createKonto(KontoDaten konto) {
        CreateKontoRequest request = CreateKontoRequest.newBuilder()
                .setKonto(konto)
                .build();
        CreateKontoResponse response = stub.createKonto(request);
        return response.getKonto();
    }

    /**
     * Ruft die Kontodaten für eine bestimmte Benutzer-ID ab.
     * Diese Methode sendet einen FindByBenutzerIdRequest an den Konto-Service,
     * um die Kontodaten des angegebenen Benutzers abzurufen.
     *
     * @param benutzerId Die ID des Benutzers, dessen Kontodaten abgerufen werden sollen.
     * @return Die Kontodaten des angegebenen Benutzers.
     */
    public KontoDaten findByBenutzerId(String benutzerId) {
        FindByBenutzerIdRequest request = FindByBenutzerIdRequest.newBuilder()
                .setBenutzerId(benutzerId)
                .build();
        FindByBenutzerIdResponse response = stub.findByBenutzerId(request);
        return response.getKonto();
    }
}
