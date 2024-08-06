package de.th.koeln.finanzdatenservice.grpc;

import de.th.koeln.finanzdaten.grpc.*;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * gRPC-Service-Implementierung für Finanzdaten.
 *
 * Diese Klasse stellt die gRPC-Methoden für das Finanzdaten-Service zur Verfügung,
 * einschließlich der Abfrage von Einnahmen und Ausgaben, der Berechnung von Summen
 * und der Verwaltung finanzieller Ziele.
 */
@GrpcService
public class FinanzdatenServiceImpl extends FinanzdatenServiceGrpc.FinanzdatenServiceImplBase {

    private final EinnahmeService einnahmeService;
    private final AusgabeService ausgabeService;
    private final KontoGrpcClient kontoClient;
    private final FinanzielleZielService zieleService;

    /**
     * Konstruktor für FinanzdatenServiceImpl.
     *
     * @param einnahmeService Der Dienst zur Verwaltung von Einnahmen.
     * @param ausgabeService Der Dienst zur Verwaltung von Ausgaben.
     * @param kontoClient Der gRPC-Client für Kontodienste.
     * @param zieleService Der Dienst zur Verwaltung finanzieller Ziele.
     */
    @Autowired
    public FinanzdatenServiceImpl(EinnahmeService einnahmeService, AusgabeService ausgabeService
            , KontoGrpcClient kontoClient, FinanzielleZielService zieleService) {
        this.einnahmeService = einnahmeService;
        this.ausgabeService = ausgabeService;
        this.kontoClient = kontoClient;
        this.zieleService = zieleService;
    }

    /**
     * Ruft die Einnahmen des aktuellen Monats ab.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Abfrage der Einnahmen des aktuellen
     * Monats für ein gegebenes Konto und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Abfrage der Einnahmen des aktuellen Monats.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void getEinnahmenDesMonats(GetEinnahmenDesMonatsRequest request, StreamObserver<GetEinnahmenDesMonatsResponse> responseObserver) {
        Long kontoId = Long.parseLong(request.getKontoId());
        Optional<KontoDaten> kontoDaten = Optional.ofNullable(this.kontoClient.findKontoById(request.getKontoId()));
        if (kontoDaten.isPresent()) {
            Set<Einnahme> einnahmen = this.einnahmeService.holeEinnahmenAktuellesDatum(kontoId);
            GetEinnahmenDesMonatsResponse response = GetEinnahmenDesMonatsResponse.newBuilder()
                    .addAllEinnahmen(einnahmen.stream().map(einnahme ->
                            {
                                FinanzdatenDTO.Builder finanzDTO = FinanzdatenDTO.newBuilder();

                                if (einnahme.getBenutzerID() != null) {
                                    finanzDTO.setBenutzerID(einnahme.getBenutzerID());
                                }

                                if(einnahme.getEinnahmeKategorie() != null) {
                                    finanzDTO.setKategorie(einnahme.getEinnahmeKategorie().toString().toUpperCase());
                                }

                                if (einnahme.getBezeichnung() != null) {
                                    finanzDTO.setBezeichnung(einnahme.getBezeichnung());
                                }

                                if (einnahme.getBeschreibung() != null) {
                                    finanzDTO.setBeschreibung(einnahme.getBeschreibung());
                                }

                                if (einnahme.getDatum() != null) {
                                    finanzDTO.setDatum(einnahme.getDatum().toString());
                                }

                                if (einnahme.getBetrag() != null) {
                                    finanzDTO.setBetrag(einnahme.getBetrag().toString());
                                }
                                return finanzDTO.build();
                            })
                            .collect(Collectors.toList()))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden"));
        }
    }

    /**
     * Ruft die Ausgaben des aktuellen Monats ab.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Abfrage der Ausgaben des aktuellen
     * Monats für ein gegebenes Konto und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Abfrage der Ausgaben des aktuellen Monats.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
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
                            .collect(Collectors.toList()))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden"));
        }
    }

    /**
     * Ruft alle Einnahmen für ein gegebenes Konto ab.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Abfrage aller Einnahmen für ein gegebenes Konto
     * und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Abfrage aller Einnahmen.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void getAlleEinnahmen(GetAlleEinnahmenRequest request, StreamObserver<GetAlleEinnahmenResponse> responseObserver) {
        Long kontoId = Long.parseLong(request.getKontoId());
        Optional<KontoDaten> kontoDaten = Optional.ofNullable(this.kontoClient.findKontoById(request.getKontoId()));
        if (kontoDaten.isPresent()) {
            Set<Einnahme> einnahmen = this.einnahmeService.findAllByKontoId(kontoId);
            GetAlleEinnahmenResponse response = GetAlleEinnahmenResponse.newBuilder()
                    .addAllEinnahmen(einnahmen.stream().map(einnahme ->
                            {
                                FinanzdatenDTO.Builder finanzDTO = FinanzdatenDTO.newBuilder();

                                if (einnahme.getBenutzerID() != null) {
                                    finanzDTO.setBenutzerID(einnahme.getBenutzerID());
                                }
                                if(einnahme.getEinnahmeKategorie() != null) {
                                    finanzDTO.setKategorie(einnahme.getEinnahmeKategorie().toString().toUpperCase());
                                }

                                if (einnahme.getBezeichnung() != null) {
                                    finanzDTO.setBezeichnung(einnahme.getBezeichnung());
                                }

                                if (einnahme.getBeschreibung() != null) {
                                    finanzDTO.setBeschreibung(einnahme.getBeschreibung());
                                }

                                if (einnahme.getDatum() != null) {
                                    finanzDTO.setDatum(einnahme.getDatum().toString());
                                }

                                if (einnahme.getBetrag() != null) {
                                    finanzDTO.setBetrag(einnahme.getBetrag().toString());
                                }
                                return finanzDTO.build();
                            })
                            .collect(Collectors.toList()))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden"));
        }
    }

    /**
     * Ruft alle Ausgaben für ein gegebenes Konto ab.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Abfrage aller Ausgaben für ein gegebenes Konto
     * und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Abfrage aller Ausgaben.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void getAlleAusgaben(GetAlleAusgabenRequest request, StreamObserver<GetAlleAusgabenResponse> responseObserver) {
        Long kontoId = Long.parseLong(request.getKontoId());
        Optional<KontoDaten> kontoDaten = Optional.ofNullable(this.kontoClient.findKontoById(request.getKontoId()));
        if (kontoDaten.isPresent()) {
            Set<Ausgabe> ausgaben = this.ausgabeService.findAllByKontoId(kontoId);
            GetAlleAusgabenResponse response = GetAlleAusgabenResponse.newBuilder()
                    .addAllAusgaben(ausgaben.stream().map(ausgabe ->
                            {
                                FinanzdatenDTO.Builder finanzDTO = FinanzdatenDTO.newBuilder();

                                if (ausgabe.getBenutzerID() != null) {
                                    finanzDTO.setBenutzerID(ausgabe.getBenutzerID());
                                }
                                if(ausgabe.getAusgabeKategorie() != null) {
                                    finanzDTO.setKategorie(ausgabe.getAusgabeKategorie().toString().toUpperCase());
                                }

                                if (ausgabe.getBezeichnung() != null) {
                                    finanzDTO.setBezeichnung(ausgabe.getBezeichnung());
                                }

                                if (ausgabe.getBeschreibung() != null) {
                                    finanzDTO.setBeschreibung(ausgabe.getBeschreibung());
                                }

                                if (ausgabe.getDatum() != null) {
                                    finanzDTO.setDatum(ausgabe.getDatum().toString());
                                }

                                if (ausgabe.getBetrag() != null) {
                                    finanzDTO.setBetrag(ausgabe.getBetrag().toString());
                                }
                                return finanzDTO.build();
                            })
                            .collect(Collectors.toList()))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new NotFoundException("Konto mit der ID " + kontoId + " konnte nicht gefunden werden"));
        }
    }

    /**
     * Ruft alle finanziellen Ziele ab.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Abfrage aller finanziellen Ziele
     * und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Abfrage der finanziellen Ziele.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void getFinanzielleZiele(GetFinanzielleZieleRequest request, StreamObserver<GetFinanzielleZieleResponse> responseObserver) {

        List<FinanzielleZiel> finanzielleZiele = this.zieleService.findAll();
        GetFinanzielleZieleResponse response = GetFinanzielleZieleResponse.newBuilder()
                .addAllZiele(finanzielleZiele.stream().map(ziele -> {

                            FinanzdatenDTO.Builder finanzDTO = FinanzdatenDTO.newBuilder();
                            if (ziele.getBenutzerID() != null) {
                                finanzDTO.setBenutzerID(ziele.getBenutzerID());
                            }

                            if (ziele.getSparbetrag() != null) {
                                finanzDTO.setSparBetrag(ziele.getSparbetrag().floatValue());
                            }

                            if (ziele.getFaelligkeitdatum() != null) {
                                finanzDTO.setFaelligkeitDatum(ziele.getFaelligkeitdatum().toString());
                            }

                            if (ziele.getBezeichnung() != null) {
                                finanzDTO.setBezeichnung(ziele.getBezeichnung());
                            }
                            return finanzDTO.build();

                        })
                        .collect(Collectors.toList()))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Ruft die Summe der Einnahmen des aktuellen Monats ab.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Berechnung der Summe der Einnahmen
     * des aktuellen Monats für ein gegebenes Konto und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Berechnung der Einnahmensumme.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void getEinnahmenSumme(GetEinnahmenSummeRequest request, StreamObserver<GetEinnahmenSummeResponse> responseObserver) {
        Long kontoId = Long.parseLong(request.getKontoId());
        BigDecimal summe = this.einnahmeService.getSummeEinnahmenDesMonat(kontoId);
        GetEinnahmenSummeResponse response = GetEinnahmenSummeResponse.newBuilder()
                .setEinnahmenSumme(summe.doubleValue())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    /**
     * Ruft die Summe der Ausgaben des aktuellen Monats ab.
     *
     * Diese Methode empfängt eine gRPC-Anfrage zur Berechnung der Summe der Ausgaben
     * des aktuellen Monats für ein gegebenes Konto und übermittelt die Antwort.
     *
     * @param request Die Anfrage zur Berechnung der Ausgabensumme.
     * @param responseObserver Der StreamObserver zur Übermittlung der Antwort.
     */
    @Override
    public void getAusgabenSumme(GetAusgabenSummeRequest request, StreamObserver<GetAusgabenSummeResponse> responseObserver) {
        Long kontoId = Long.parseLong(request.getKontoId());
        BigDecimal summe = this.ausgabeService.getSummeAusgabenDesMonat(kontoId);
        GetAusgabenSummeResponse response = GetAusgabenSummeResponse.newBuilder()
                .setAusgabensumme(summe.doubleValue())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
