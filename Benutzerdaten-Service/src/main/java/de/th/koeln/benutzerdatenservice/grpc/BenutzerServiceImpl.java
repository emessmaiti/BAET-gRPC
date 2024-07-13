package de.th.koeln.benutzerdatenservice.grpc;

import de.th.koeln.benutzerdatenservice.entities.Benutzerdaten;
import de.th.koeln.benutzerdatenservice.entities.Geschlecht;
import de.th.koeln.benutzerdatenservice.services.BenutzerService;
import de.th.koeln.benutzerservice.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class BenutzerServiceImpl extends BenutzerServiceGrpc.BenutzerServiceImplBase {

    private final BenutzerService benutzerService;

    @Autowired
    public BenutzerServiceImpl(BenutzerService benutzerService) {
        this.benutzerService = benutzerService;
    }

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

    @Override
    public void updateLetzteAnmeldung(UpdateLetzteAnmeldungRequest request, StreamObserver<Empty> response) {
        this.benutzerService.updateLastLogin(request.getSub());
        response.onNext(Empty.newBuilder().build());
        response.onCompleted();
    }
}
