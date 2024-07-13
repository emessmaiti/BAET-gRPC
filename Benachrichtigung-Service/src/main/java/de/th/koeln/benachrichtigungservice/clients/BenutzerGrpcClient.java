package de.th.koeln.benachrichtigungservice.clients;

import de.th.koeln.benachrichtigungservice.exceptions.NotFoundException;
import de.th.koeln.benutzerservice.grpc.BenutzerDaten;
import de.th.koeln.benutzerservice.grpc.BenutzerServiceGrpc;
import de.th.koeln.benutzerservice.grpc.GetBenutzerBySubRequest;
import de.th.koeln.benutzerservice.grpc.GetBenutzerBySubResponse;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class BenutzerGrpcClient {
    @GrpcClient("benutzer-service")
    private BenutzerServiceGrpc.BenutzerServiceBlockingStub stub;

    public BenutzerDaten findBenutzerById(String id) {
        GetBenutzerBySubRequest request = GetBenutzerBySubRequest.newBuilder()
                .setSub(id)
                .build();
        try {
            GetBenutzerBySubResponse response = stub.getBenutzerBySub(request);
            return response.getBenutzer();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Benutzer not found");
        }
    }
}
