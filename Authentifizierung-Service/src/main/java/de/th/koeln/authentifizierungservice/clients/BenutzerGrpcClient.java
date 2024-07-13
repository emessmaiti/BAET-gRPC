package de.th.koeln.authentifizierungservice.clients;

import de.th.koeln.benutzerservice.grpc.*;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BenutzerGrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(BenutzerGrpcClient.class);

    @GrpcClient("benutzer-service") // Ensure the correct name
    private BenutzerServiceGrpc.BenutzerServiceBlockingStub stub;

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
