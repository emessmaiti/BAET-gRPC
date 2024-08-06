package de.th.koeln.benachrichtigungservice.dtos;

import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mapper-Klasse zur Umwandlung von FinanzdatenDTO in ZieleDTO.
 *
 * Diese Klasse stellt Methoden bereit, um FinanzdatenDTO-Objekte aus dem gRPC-Service in
 * ZieleDTO-Objekte zu konvertieren.
 */
public class ZieleMapper {

    /**
     * Konvertiert FinanzdatenDTO in ein ZieleDTO.
     *
     * Diese Methode nimmt ein FinanzdatenDTO-Objekt und konvertiert es in ein
     * ZieleDTO-Objekt, indem sie die relevanten Felder zuweist.
     *
     * @param finanzdatenDTO Die Finanzdaten aus dem gRPC-Service.
     * @return Das konvertierte ZieleDTO.
     */
    public static ZieleDTO toDto(FinanzdatenDTO finanzdatenDTO) {
        ZieleDTO dto = new ZieleDTO();
        dto.setBenutzerId(finanzdatenDTO.getBenutzerID());
        dto.setSparbetrag(BigDecimal.valueOf(finanzdatenDTO.getSparBetrag()));
        dto.setFaelligkeitDatum(LocalDate.parse(finanzdatenDTO.getFaelligkeitDatum()));
        dto.setZielName(finanzdatenDTO.getBezeichnung());

        return dto;
    }
}
