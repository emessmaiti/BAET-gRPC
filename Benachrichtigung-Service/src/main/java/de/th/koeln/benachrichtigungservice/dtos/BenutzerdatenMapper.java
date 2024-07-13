package de.th.koeln.benachrichtigungservice.dtos;

import de.th.koeln.benutzerservice.grpc.BenutzerDaten;

public class BenutzerdatenMapper {

    public static BenutzerDTO toDto(BenutzerDaten benutzerDaten) {
        BenutzerDTO dto = new BenutzerDTO();
        dto.setBenutzerID(benutzerDaten.getId());
        dto.setEmail(benutzerDaten.getEmail());

        return dto;
    }
}
