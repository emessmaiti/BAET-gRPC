package de.th.koeln.benutzerdatenservice.grpc;

import de.th.koeln.benutzerdatenservice.entities.Benutzerdaten;
import de.th.koeln.benutzerdatenservice.entities.Geschlecht;
import de.th.koeln.benutzerdatenservice.services.BenutzerService;
import de.th.koeln.benutzerservice.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * gRPC-Service-Implementierung für Benutzerdaten.
 *
 * Diese Klasse stellt die gRPC-Methoden für das Benutzer-Service zur Verfügung,
 * einschließlich der Erstellung eines Benutzers, des Abrufens von Benutzerdaten
 * anhand der Sub und der Aktualisierung der letzten Anmeldung.
 */
@GrpcService
public class BenutzerServiceImpl extends BenutzerServiceGrpc.BenutzerServiceImplBase {

    private final BenutzerService benutzerService;

    /**
     * Konstruktor für BenutzerServiceImpl.
     *
     * @param benutzerService Der Dienst zur Verwaltung von Benutzerdaten.
     */
    @Autowired
    public BenutzerServiceImpl(BenutzerService benutzerService) {
        this.benutzerService = benutzerService;
    }

    /**
     * Erstellt einen neuen Benutzer.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Erstellung eines neuen Benutzers,
     * konvertiert die Anfrage in ein Benutzerdaten-Objekt und speichert es.
     *
     * @param request Die Anfrage zur Erstellung eines neuen Benutzers.
     * @param response Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void createBenutzer(CreateBenutzerRequest request, StreamObserver<Empty> response) {
        Benutzerdaten benutzer = new Benutzerdaten();
        benutzer.setVorname(request.getBenutzer().getVorname());
        benutzer.setNachname(request.getBenutzer().getNachname());
        benutzer.setEmail(request.getBenutzer().getEmail());
        benutzer.setGeschlecht(Geschlecht.valueOf(request.getBenutzer().getGeschlecht().toUpperCase()));
        benutzer.setSub(request.getBenutzer().getSub());
        benutzerService.speichern(benutzer);
        response.onNext(Empty.newBuilder().build());
        response.onCompleted();
    }

    /**
     * Ruft Benutzerdaten anhand der Sub ab.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Abfrage von Benutzerdaten anhand
     * der Sub, konvertiert die Benutzerdaten in ein gRPC-kompatibles Format und
     * übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Abfrage von Benutzerdaten.
     * @param response Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void getBenutzerBySub(GetBenutzerBySubRequest request, StreamObserver<GetBenutzerBySubResponse> response) {
        Benutzerdaten benutzer = this.benutzerService.findBenutzerBySub(request.getSub());
        if (benutzer != null) {
            BenutzerDaten benutzerDaten = BenutzerDaten.newBuilder()
                    .setId(String.valueOf(benutzer.getId()))
                    .setVorname(benutzer.getVorname())
                    .setNachname(benutzer.getNachname())
                    .setEmail(benutzer.getEmail())
                    .setGeschlecht(benutzer.getGeschlecht().name().toLowerCase())
                    .setSub(benutzer.getSub())
                    .build();

            GetBenutzerBySubResponse benutzerResponse = GetBenutzerBySubResponse.newBuilder()
                    .setBenutzer(benutzerDaten).build();
            response.onNext(benutzerResponse);
        } else {
            response.onNext(GetBenutzerBySubResponse.getDefaultInstance());
        }
        response.onCompleted();
    }

    /**
     * Aktualisiert die letzte Anmeldung eines Benutzers.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Aktualisierung der letzten
     * Anmeldung eines Benutzers anhand der Sub und führt die Aktualisierung durch.
     *
     * @param request Die Anfrage zur Aktualisierung der letzten Anmeldung.
     * @param response Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void updateLetzteAnmeldung(UpdateLetzteAnmeldungRequest request, StreamObserver<Empty> response) {
        this.benutzerService.updateLastLogin(request.getSub());
        response.onNext(Empty.newBuilder().build());
        response.onCompleted();
    }
}
