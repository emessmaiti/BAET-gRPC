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

@Service
public class ZieleGrpClient {

    @GrpcClient("finanzdaten-service")
    private FinanzdatenServiceGrpc.FinanzdatenServiceBlockingStub stub;

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
