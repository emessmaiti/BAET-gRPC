package de.th.koeln.transaktionenservice.dto;

import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;

public class FinanzdatenMapper {

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
