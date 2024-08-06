package de.th.koeln.kontoservice.dto;

import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;

/**
 * Mapper-Klasse zur Umwandlung zwischen FinanzdatenDTO und FinanzdatenDto.
 *
 * Diese Klasse stellt Methoden bereit, um FinanzdatenDTO-Objekte aus dem gRPC-Service in
 * FinanzdatenDto-Objekte und umgekehrt zu konvertieren.
 */
public class FinanzdatenMapper {

    /**
     * Konvertiert ein FinanzdatenDTO in ein FinanzdatenDto.
     *
     * Diese Methode nimmt ein FinanzdatenDTO-Objekt und konvertiert es in ein
     * FinanzdatenDto-Objekt, indem sie die relevanten Felder zuweist.
     *
     * @param proto Das FinanzdatenDTO-Objekt aus dem gRPC-Service.
     * @return Das konvertierte FinanzdatenDto-Objekt.
     */
    public static FinanzdatenDto toDto(FinanzdatenDTO proto) {
        FinanzdatenDto dto = new FinanzdatenDto();
        dto.setBenutzerID(proto.getBenutzerID());
        dto.setKategorie(proto.getKategorie());
        dto.setBezeichnung(proto.getBezeichnung());
        dto.setBeschreibung(proto.getBeschreibung());
        dto.setDatum(proto.getDatum());
        dto.setBudget(proto.getBudget());
        dto.setBetrag(proto.getBetrag());
        return dto;
    }

    /**
     * Konvertiert ein FinanzdatenDto in ein FinanzdatenDTO.
     *
     * Diese Methode nimmt ein FinanzdatenDto-Objekt und konvertiert es in ein
     * FinanzdatenDTO-Objekt, indem sie die relevanten Felder zuweist.
     *
     * @param dto Das FinanzdatenDto-Objekt.
     * @return Das konvertierte FinanzdatenDTO-Objekt.
     */
    public static FinanzdatenDTO toProto(FinanzdatenDto dto) {
        return FinanzdatenDTO.newBuilder()
                .setBenutzerID(dto.getBenutzerID())
                .setKategorie(dto.getKategorie())
                .setBezeichnung(dto.getBezeichnung())
                .setBeschreibung(dto.getBeschreibung())
                .setDatum(dto.getDatum())
                .setBudget(dto.getBudget())
                .setBetrag(dto.getBetrag())
                .build();
    }
}
