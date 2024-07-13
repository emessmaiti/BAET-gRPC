package de.th.koeln.transaktionenservice.clients;

import de.th.koeln.finanzdaten.grpc.*;
import de.th.koeln.transaktionenservice.exceptions.NotFoundException;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FinanzdatenGrpcClient {

    @GrpcClient("finanzdaten-service")
    private FinanzdatenServiceGrpc.FinanzdatenServiceBlockingStub stub;

    public List<FinanzdatenDTO> getAllEinnahmen(Long kontoId) {
        GetEinnahmenDesMonatsRequest einnahmenRequest = GetEinnahmenDesMonatsRequest.newBuilder()
                .setKontoId(kontoId.toString())
                .build();
        try {
            GetEinnahmenDesMonatsResponse einnahmeResponse = stub.getEinnahmenDesMonats(einnahmenRequest);
            return einnahmeResponse.getEinnahmenList();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden");

        }
    }

    public List<FinanzdatenDTO> getAllAusgaben(Long kontoId) {
        GetAusgabenDesMonatsRequest ausgabenRequest = GetAusgabenDesMonatsRequest.newBuilder()
                .setKontoId(kontoId.toString())
                .build();
        try {
            GetAusgabenDesMonatsResponse ausgabeResponse = stub.getAusgabenDesMonats(ausgabenRequest);
            return ausgabeResponse.getAusgabenList();
        } catch (StatusRuntimeException e) {
            throw new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden");

        }
    }


}
