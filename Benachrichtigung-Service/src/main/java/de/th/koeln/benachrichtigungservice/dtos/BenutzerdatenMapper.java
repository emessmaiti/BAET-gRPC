package de.th.koeln.benachrichtigungservice.dtos;

import de.th.koeln.benutzerservice.grpc.BenutzerDaten;

/**
 * Mapper-Klasse zur Umwandlung von Benutzerdaten in ein DTO (Data Transfer Object).
 *
 * Diese Klasse stellt Methoden bereit, um Benutzerdaten aus dem gRPC-Service in ein
 * BenutzerDTO zu konvertieren.
 */
public class BenutzerdatenMapper {

    /**
     * Konvertiert Benutzerdaten in ein BenutzerDTO.
     *
     * Diese Methode nimmt ein BenutzerDaten-Objekt und konvertiert es in ein
     * BenutzerDTO-Objekt, indem sie die relevanten Felder zuweist.
     *
     * @param benutzerDaten Die Benutzerdaten aus dem gRPC-Service.
     * @return Das konvertierte BenutzerDTO.
     */
    public static BenutzerDTO toDto(BenutzerDaten benutzerDaten) {
        BenutzerDTO dto = new BenutzerDTO();
        dto.setBenutzerID(benutzerDaten.getId());
        dto.setEmail(benutzerDaten.getEmail());

        return dto;
    }
}
