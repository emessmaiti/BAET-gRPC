package de.th.koeln.benachrichtigungservice.dtos;

import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ZieleMapper {

    public static ZieleDTO toDto(FinanzdatenDTO finanzdatenDTO) {
        ZieleDTO dto = new ZieleDTO();
        dto.setBenutzerId(finanzdatenDTO.getBenutzerID());
        dto.setSparbetrag(BigDecimal.valueOf(finanzdatenDTO.getSparBetrag()));
        dto.setFaelligkeitDatum(LocalDate.parse(finanzdatenDTO.getFaelligkeitDatum()));
        dto.setZielName(finanzdatenDTO.getBezeichnung());

        return dto;
    }
}
