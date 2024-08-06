package de.th.koeln.authentifizierungservice.clients;

import de.th.koeln.benutzerservice.grpc.*;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service-Klasse für die Kommunikation mit dem Benutzer-Service über gRPC.
 *
 * Diese Klasse stellt Methoden bereit, um Benutzerdaten vom Benutzer-Service abzurufen,
 * neue Benutzer zu erstellen und die letzte Anmeldung eines Benutzers zu aktualisieren.
 * Die Kommunikation erfolgt über die gRPC-Schnittstelle.
 */
@Service
public class BenutzerGrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(BenutzerGrpcClient.class);

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
     * Holt die Benutzerdaten basierend auf der Sub.
     *
     * Diese Methode sendet eine Anfrage an den Benutzer-Service, um die Benutzerdaten
     * für eine gegebene Sub (ein eindeutiger google Identifikator) abzurufen. Wenn der Benutzer
     * gefunden wird, werden die Benutzerdaten zurückgegeben. Andernfalls wird null zurückgegeben.
     *
     * @param sub Die Sub des Benutzers.
     * @return Die Benutzerdaten oder null, wenn der Benutzer nicht gefunden wird.
     * @throws StatusRuntimeException wenn die Anfrage an den gRPC-Service fehlschlägt.
     */
    public BenutzerDaten getBenutzerBySub(String sub) {
        GetBenutzerBySubRequest request = GetBenutzerBySubRequest.newBuilder()
                .setSub(sub)
                .build();
        try {
            GetBenutzerBySubResponse response = stub.getBenutzerBySub(request);
            if (response.hasBenutzer()) {
                return response.getBenutzer();
            }
            return null;
        } catch (StatusRuntimeException e) {
            logger.error("GetBenutzerBySub request failed: {}", e.getStatus(), e);
            return null;
        }
    }

    /**
     * Erstellt einen neuen Benutzer.
     *
     * Diese Methode sendet eine Anfrage an den Benutzer-Service, um einen neuen Benutzer
     * mit den angegebenen Benutzerdaten zu erstellen.
     *
     * @param benutzer Die Benutzerdaten für den neuen Benutzer.
     * @throws StatusRuntimeException wenn die Anfrage an den gRPC-Service fehlschlägt.
     */
    public void createBenutzer(BenutzerDaten benutzer) {
        CreateBenutzerRequest request = CreateBenutzerRequest.newBuilder()
                .setBenutzer(benutzer)
                .build();
        try {
            stub.createBenutzer(request);
            logger.info("CreateBenutzer request successful");
        } catch (StatusRuntimeException e) {
            logger.error("CreateBenutzer request failed: {}", e.getStatus(), e);
        }
    }

    /**
     * Aktualisiert die letzte Anmeldung eines Benutzers basierend auf der Sub.
     *
     * Diese Methode sendet eine Anfrage an den Benutzer-Service, um die letzte
     * Anmeldung eines Benutzers zu aktualisieren, basierend auf der angegebenen Sub.
     *
     * @param sub Die Sub des Benutzers.
     * @throws StatusRuntimeException wenn die Anfrage an den gRPC-Service fehlschlägt.
     */
    public void updateLetzteAnmeldung(String sub) {
        UpdateLetzteAnmeldungRequest request = UpdateLetzteAnmeldungRequest.newBuilder()
                .setSub(sub)
                .build();
        try {
            stub.updateLetzteAnmeldung(request);
            logger.info("UpdateLetzteAnmeldung request successful");
        } catch (StatusRuntimeException e) {
            logger.error("UpdateLetzteAnmeldung request failed: {}", e.getStatus(), e);
        }
    }
}
