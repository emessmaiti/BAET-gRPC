package de.th.koeln.authentifizierungservice.controllers;


import de.th.koeln.authentifizierungservice.clients.BenutzerGrpcClient;
import de.th.koeln.authentifizierungservice.clients.KontoGrpcClient;
import de.th.koeln.benutzerservice.grpc.BenutzerDaten;
import de.th.koeln.kontoservice.grpc.KontoDaten;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Controller-Klasse für die Authentifizierung und Benutzerverwaltung.
 *
 * Diese Klasse stellt Endpunkte bereit, um Authentifizierungsdetails des aktuellen Benutzers
 * abzurufen, den Login-Prozess zu verarbeiten und das Access Token des Benutzers zurückzugeben.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthentifizierungController {

    private final KontoGrpcClient kontoGrpcClient;
    private final BenutzerGrpcClient benutzerGrpcClient;

    /**
     * Konstruktor für AuthentifizierungController.
     *
     * @param kontoGrpcClient Der gRPC-Client für Kontodienste.
     * @param benutzerGrpcClient Der gRPC-Client für Benutzerdienste.
     */
    @Autowired
    public AuthentifizierungController(KontoGrpcClient kontoGrpcClient, BenutzerGrpcClient benutzerGrpcClient) {
        this.kontoGrpcClient = kontoGrpcClient;
        this.benutzerGrpcClient = benutzerGrpcClient;
    }

    /**
     * Gibt die Authentifizierungsdetails des aktuellen Benutzers zurück.
     *
     * @param authentication Die Authentifizierungsinformationen des aktuellen Benutzers.
     * @return Die Authentifizierungsinformationen.
     */
    @GetMapping("/info")
    public Authentication authenticate(Authentication authentication) {
        return authentication;
    }

    /**
     * Verarbeitet den erfolgreichen Login eines Benutzers.
     *
     * Diese Methode wird aufgerufen, wenn ein Benutzer sich erfolgreich anmeldet.
     * Sie aktualisiert die Benutzerdaten und das Konto des Benutzers und leitet dann
     * auf eine spezifische URL weiter.
     *
     * @param oidcUser Der authentifizierte OIDC-Benutzer.
     * @param response Die HTTP-Antwort.
     * @throws IOException Wenn ein I/O-Fehler auftritt.
     */
    @GetMapping("/loginSuccess")
    public void loginSuccess(@AuthenticationPrincipal OidcUser oidcUser, HttpServletResponse response) throws IOException, IOException {
        String accessToken = oidcUser.getIdToken().getTokenValue();
        String email = oidcUser.getEmail();
        String vorname = oidcUser.getGivenName();
        String nachname = oidcUser.getFamilyName();
        String geschlecht = oidcUser.getGender() != null ? oidcUser.getGender() : "UNBEKANNT";
        String sub = oidcUser.getSubject();
        LocalDateTime letzteAnmeldung = LocalDateTime.now();

        BenutzerDaten benutzer = benutzerGrpcClient.getBenutzerBySub(sub);
        KontoDaten konto = null;


        if(benutzer != null) {
            benutzerGrpcClient.updateLetzteAnmeldung(sub);
            konto = kontoGrpcClient.findByBenutzerId(sub);

            if (konto == null) {
                KontoDaten kontoDaten = KontoDaten.newBuilder()
                        .setBenutzerId(sub)
                        .setKontostand(BigDecimal.ZERO.toPlainString())
                        .build();
                kontoGrpcClient.createKonto(kontoDaten);
            }
        } else {
            BenutzerDaten benutzerDaten = BenutzerDaten.newBuilder()
                    .setVorname(vorname)
                    .setNachname(nachname)
                    .setGeschlecht(geschlecht)
                    .setEmail(email)
                    .setSub(sub)
                    .build();
            benutzerGrpcClient.createBenutzer(benutzerDaten);
            KontoDaten kontoDaten = KontoDaten.newBuilder()
                    .setBenutzerId(sub)
                    .setKontostand(BigDecimal.ZERO.toPlainString())
                    .build();
            kontoGrpcClient.createKonto(kontoDaten);
        }

        String kontoId = konto != null ? konto.getId() : "";

        response.sendRedirect("http://localhost:4200/login?access_token=" + accessToken + "&kontoId=" + kontoId);
    }

    /**
     * Gibt das Access Token des aktuellen Benutzers zurück.
     *
     * @param principal Der authentifizierte OIDC-Benutzer.
     * @return Das ID-Token des aktuellen Benutzers.
     */
    @GetMapping("/token")
    public String getToken(@AuthenticationPrincipal OidcUser principal) {
        return principal.getIdToken().getTokenValue();
    }

}
