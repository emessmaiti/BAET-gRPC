package de.th.koeln.finanzdatenservice.grpc;

import de.th.koeln.benutzerservice.grpc.BenutzerDaten;
import de.th.koeln.finanzdaten.grpc.*;
import de.th.koeln.finanzdatenservice.clients.BenutzerGrpcClient;
import de.th.koeln.finanzdatenservice.clients.KontoGrpcClient;
import de.th.koeln.finanzdatenservice.entities.Ausgabe;
import de.th.koeln.finanzdatenservice.entities.Einnahme;
import de.th.koeln.finanzdatenservice.entities.FinanzielleZiel;
import de.th.koeln.finanzdatenservice.exceptions.NotFoundException;
import de.th.koeln.finanzdatenservice.services.AusgabeService;
import de.th.koeln.finanzdatenservice.services.EinnahmeService;
import de.th.koeln.finanzdatenservice.services.FinanzielleZielService;
import de.th.koeln.kontoservice.grpc.KontoDaten;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@GrpcService
public class FinanzdatenServiceImpl extends FinanzdatenServiceGrpc.FinanzdatenServiceImplBase {

    private final EinnahmeService einnahmeService;
    private final AusgabeService ausgabeService;
    private final KontoGrpcClient kontoClient;
    private final FinanzielleZielService zieleService;
    private final BenutzerGrpcClient benutzerClient;

    @Autowired
    public FinanzdatenServiceImpl(EinnahmeService einnahmeService, AusgabeService ausgabeService
            , KontoGrpcClient kontoClient, FinanzielleZielService zieleService, BenutzerGrpcClient benutzerClient) {
        this.einnahmeService = einnahmeService;
        this.ausgabeService = ausgabeService;
        this.kontoClient = kontoClient;
        this.zieleService = zieleService;
        this.benutzerClient = benutzerClient;
    }

    @Override
    public void getEinnahmenDesMonats(GetEinnahmenDesMonatsRequest request, StreamObserver<GetEinnahmenDesMonatsResponse> responseObserver) {
        Long kontoId = Long.parseLong(request.getKontoId());
        Optional<KontoDaten> kontoDaten = Optional.ofNullable(this.kontoClient.findKontoById(request.getKontoId()));
        if (kontoDaten.isPresent()) {
            Set<Einnahme> einnahmen = this.einnahmeService.holeEinnahmenAktuellesDatum(kontoId);
            GetEinnahmenDesMonatsResponse response = GetEinnahmenDesMonatsResponse.newBuilder()
                    .addAllEinnahmen(einnahmen.stream().map(einnahme ->
                    {
                        FinanzdatenDTO.Builder finanzDTO =  FinanzdatenDTO.newBuilder();

                        if(einnahme.getBenutzerID() != null){
                           finanzDTO.setBenutzerID(einnahme.getBenutzerID());
                        }

                        if(einnahme.getEinnahmeKategorie() != null){
                            finanzDTO.setKategorie(einnahme.getEinnahmeKategorie().toString().toUpperCase());
                        }

                        if(einnahme.getBezeichnung() != null){
                            finanzDTO.setBezeichnung(einnahme.getBezeichnung());
                        }

                        if(einnahme.getBeschreibung() != null){
                            finanzDTO.setBeschreibung(einnahme.getBeschreibung());
                        }

                        if(einnahme.getDatum() != null){
                            finanzDTO.setDatum(einnahme.getDatum().toString());
                        }

                        if(einnahme.getBetrag() != null){
                            finanzDTO.setBetrag(einnahme.getBetrag().toString());
                        }
                               return finanzDTO.build();
                    })
                            .collect(Collectors.toSet()))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden"));
        }
    }

    @Override
    public void getAusgabenDesMonats(GetAusgabenDesMonatsRequest request, StreamObserver<GetAusgabenDesMonatsResponse> responseObserver) {
        Long kontoId = Long.parseLong(request.getKontoId());
        Optional<KontoDaten> kontoDaten = Optional.ofNullable(this.kontoClient.findKontoById(request.getKontoId()));
        if (kontoDaten.isPresent()) {
            Set<Ausgabe> ausgaben = this.ausgabeService.holeAusgabenAktuellesDatum(kontoId);
            GetAusgabenDesMonatsResponse response = GetAusgabenDesMonatsResponse.newBuilder()
                    .addAllAusgaben(ausgaben.stream().map(ausgabe -> FinanzdatenDTO.newBuilder()
                                    .setBenutzerID(ausgabe.getBenutzerID())
                                    .setKategorie(ausgabe.getAusgabeKategorie().toString().toUpperCase())
                                    .setBezeichnung(ausgabe.getBezeichnung())
                                    .setBeschreibung(ausgabe.getBeschreibung())
                                    .setDatum(ausgabe.getDatum().toString())
                                    .setBetrag(ausgabe.getBetrag().toString())
                                    .setBudget(ausgabe.getBudget().toString())
                                    .build())
                            .collect(Collectors.toSet()))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden"));
        }
    }

    @Override
    public void getFinanzielleZiele (GetFinanzielleZieleRequest request, StreamObserver<GetFinanzielleZieleResponse> responseObserver) {

            List<FinanzielleZiel> finanzielleZiele = (List<FinanzielleZiel>) this.zieleService.findAll();
            GetFinanzielleZieleResponse response = GetFinanzielleZieleResponse.newBuilder()
                    .addAllZiele(finanzielleZiele.stream().map(ziele -> {

                                FinanzdatenDTO.Builder finanzDTO =  FinanzdatenDTO.newBuilder();
                                if(ziele.getBenutzerID() != null){
                                    finanzDTO.setBenutzerID(ziele.getBenutzerID());
                                }

                                if(ziele.getSparbetrag() != null){
                                    finanzDTO.setSparBetrag(ziele.getSparbetrag().floatValue());
                                }

                                if(ziele.getFaelligkeitdatum() != null){
                                    finanzDTO.setFaelligkeitDatum(ziele.getFaelligkeitdatum().toString());
                                }

                                if (ziele.getBezeichnung() != null){
                                    finanzDTO.setBezeichnung(ziele.getBezeichnung());
                                }
                                return finanzDTO.build();

                            })
                            .collect(Collectors.toList()))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
    }

}
