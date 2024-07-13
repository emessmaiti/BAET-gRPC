package de.th.koeln.kontoservice.grpc;

import de.th.koeln.kontoservice.entities.Kontodaten;
import de.th.koeln.kontoservice.services.KontodatenService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

@GrpcService
public class KontoServiceImpl extends KontoServiceGrpc.KontoServiceImplBase {

    private final KontodatenService service;

    @Autowired
    public KontoServiceImpl(KontodatenService service) {
        this.service = service;
    }

    public void createKonto(CreateKontoRequest request, StreamObserver<CreateKontoResponse> responseObserver) {
        Kontodaten konto = new Kontodaten();
        konto.setBenutzerId(request.getKonto().getBenutzerId());
        konto.setKontostand(new BigDecimal(request.getKonto().getKontostand()));

        Kontodaten savedKonto = service.save(konto);
        CreateKontoResponse response = CreateKontoResponse.newBuilder()
                .setKonto(KontoDaten.newBuilder()
                        .setId(String.valueOf(savedKonto.getId()))
                        .setBenutzerId(savedKonto.getBenutzerId())
                        .setKontostand(savedKonto.getKontostand().toPlainString())
                        .build()
                )
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void findByBenutzerId(FindByBenutzerIdRequest request, StreamObserver<FindByBenutzerIdResponse> responseObserver) {
        Optional<Kontodaten> kontodaten = service.findByBenutzerId(request.getBenutzerId());
        if (kontodaten.isPresent()) {
            Kontodaten konto = kontodaten.get();
            FindByBenutzerIdResponse response = FindByBenutzerIdResponse.newBuilder()
                    .setKonto(KontoDaten.newBuilder()
                            .setId(String.valueOf(konto.getId()))
                            .setBenutzerId(konto.getBenutzerId())
                            .setKontostand(konto.getKontostand().toPlainString())
                            .build()
                    )
                    .build();
            responseObserver.onNext(response);
        } else {
            responseObserver.onError(new RuntimeException("Konto nicht gefunden"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void findKontoById(FindKontoByIdRequest request, StreamObserver<FindKontoByIdResponse> responseObserver) {
        Optional<Kontodaten> kontodaten = this.service.findKontoById(Long.valueOf(request.getKontoId()));
        if (kontodaten.isPresent()) {
            Kontodaten konto = kontodaten.get();
            FindKontoByIdResponse response = FindKontoByIdResponse.newBuilder()
                    .setKonto(KontoDaten.newBuilder()
                            .setId(String.valueOf(konto.getId()))
                            .setBenutzerId(konto.getBenutzerId())
                            .setKontostand(konto.getKontostand().toPlainString())
                            .build()
                    )
                    .build();
            responseObserver.onNext(response);
        } else {
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("Konto mit der ID " + request.getKontoId() + " konnte nicht gefunden werden")));
        }
        responseObserver.onCompleted();
    }

}
