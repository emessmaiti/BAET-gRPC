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

/**
 * gRPC-Service-Implementierung für Kontodaten.
 *
 * Diese Klasse stellt die gRPC-Methoden für das Konto-Service zur Verfügung,
 * einschließlich der Erstellung eines Kontos und der Abfrage von Kontodaten
 * anhand der Benutzer-ID oder der Konto-ID.
 */
@GrpcService
public class KontoServiceImpl extends KontoServiceGrpc.KontoServiceImplBase {

    private final KontodatenService service;

    /**
     * Konstruktor für KontoServiceImpl.
     *
     * @param service Der Dienst zur Verwaltung von Kontodaten.
     */
    @Autowired
    public KontoServiceImpl(KontodatenService service) {
        this.service = service;
    }

    /**
     * Erstellt ein neues Konto.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Erstellung eines neuen Kontos,
     * konvertiert die Anfrage in ein Kontodaten-Objekt und speichert es.
     *
     * @param request Die Anfrage zur Erstellung eines neuen Kontos.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    public void createKonto(CreateKontoRequest request, StreamObserver<CreateKontoResponse> responseObserver) {
        // Erstellt ein neues Kontodaten-Objekt und setzt die Werte aus der Anfrage
        Kontodaten konto = new Kontodaten();
        konto.setBenutzerId(request.getKonto().getBenutzerId());
        konto.setKontostand(new BigDecimal(request.getKonto().getKontostand()));

        // Speichert das Konto in der Datenbank
        Kontodaten savedKonto = service.save(konto);

        // Erstellt die Antwort mit den gespeicherten Kontodaten
        CreateKontoResponse response = CreateKontoResponse.newBuilder()
                .setKonto(KontoDaten.newBuilder()
                        .setId(String.valueOf(savedKonto.getId()))
                        .setBenutzerId(savedKonto.getBenutzerId())
                        .setKontostand(savedKonto.getKontostand().toPlainString())
                        .build()
                )
                .build();

        // Sendet die Antwort an den Client und schließt den Stream
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Findet ein Konto basierend auf der Benutzer-ID.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Abfrage von Kontodaten
     * anhand der Benutzer-ID und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Abfrage von Kontodaten.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void findByBenutzerId(FindByBenutzerIdRequest request, StreamObserver<FindByBenutzerIdResponse> responseObserver) {
        // Sucht das Konto in der Datenbank anhand der Benutzer-ID
        Optional<Kontodaten> kontodaten = service.findByBenutzerId(request.getBenutzerId());

        if (kontodaten.isPresent()) {
            Kontodaten konto = kontodaten.get();

            // Erstellt die Antwort mit den Kontodaten
            FindByBenutzerIdResponse response = FindByBenutzerIdResponse.newBuilder()
                    .setKonto(KontoDaten.newBuilder()
                            .setId(String.valueOf(konto.getId()))
                            .setBenutzerId(konto.getBenutzerId())
                            .setKontostand(konto.getKontostand().toPlainString())
                            .build()
                    )
                    .build();

            // Sendet die Antwort an den Client
            responseObserver.onNext(response);
        } else {
            // Sendet eine Fehlermeldung, wenn das Konto nicht gefunden wird
            responseObserver.onError(new RuntimeException("Konto nicht gefunden"));
        }

        // Schließt den Stream
        responseObserver.onCompleted();
    }

    /**
     * Findet ein Konto basierend auf der Konto-ID.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Abfrage von Kontodaten
     * anhand der Konto-ID und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Abfrage von Kontodaten.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void findKontoById(FindKontoByIdRequest request, StreamObserver<FindKontoByIdResponse> responseObserver) {
        // Sucht das Konto in der Datenbank anhand der Konto-ID
        Optional<Kontodaten> kontodaten = this.service.findKontoById(Long.valueOf(request.getKontoId()));

        if (kontodaten.isPresent()) {
            Kontodaten konto = kontodaten.get();

            // Erstellt die Antwort mit den Kontodaten
            FindKontoByIdResponse response = FindKontoByIdResponse.newBuilder()
                    .setKonto(KontoDaten.newBuilder()
                            .setId(String.valueOf(konto.getId()))
                            .setBenutzerId(konto.getBenutzerId())
                            .setKontostand(konto.getKontostand().toPlainString())
                            .build()
                    )
                    .build();

            // Sendet die Antwort an den Client
            responseObserver.onNext(response);
        } else {
            // Sendet eine Fehlermeldung, wenn das Konto nicht gefunden wird
            responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("Konto mit der ID " + request.getKontoId() + " konnte nicht gefunden werden")));
        }

        // Schließt den Stream
        responseObserver.onCompleted();
    }
}
