package de.th.koeln.benachrichtigungservice.clients;

import de.th.koeln.benachrichtigungservice.exceptions.NotFoundException;
import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;
import de.th.koeln.finanzdaten.grpc.FinanzdatenServiceGrpc;
import de.th.koeln.finanzdaten.grpc.GetFinanzielleZieleRequest;
import de.th.koeln.finanzdaten.grpc.GetFinanzielleZieleResponse;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-Klasse für die Kommunikation mit dem Finanzdaten-Service über gRPC.
 *
 * Diese Klasse stellt Methoden bereit, um finanzielle Ziele vom Finanzdaten-Service
 * abzurufen. Die Kommunikation erfolgt über die gRPC-Schnittstelle.
 */
@Service
public class ZieleGrpClient {

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
     * Ruft die finanziellen Ziele ab.
     *
     * Diese Methode sendet eine Anfrage an den Finanzdaten-Service, um die finanziellen
     * Ziele abzurufen. Wenn die Ziele erfolgreich abgerufen werden, wird eine Liste
     * von FinanzdatenDTO zurückgegeben. Andernfalls wird eine NotFoundException ausgelöst.
     *
     * @return Eine Liste der finanziellen Ziele.
     * @throws NotFoundException wenn die finanziellen Ziele nicht gefunden werden.
     */
    public List<FinanzdatenDTO> getZiele() {
        GetFinanzielleZieleRequest request = GetFinanzielleZieleRequest.newBuilder()
                .build();
        try {
            GetFinanzielleZieleResponse response = stub.getFinanzielleZiele(request);
            return response.getZieleList();
        } catch (StatusRuntimeException e){
            throw new NotFoundException("Benutzer not found");
        }
    }

}
