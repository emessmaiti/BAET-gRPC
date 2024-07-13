package de.th.koeln.transaktionenservice.services;

import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;
import de.th.koeln.transaktionenservice.clients.FinanzdatenGrpcClient;
import de.th.koeln.transaktionenservice.dto.FinanzdatenDto;
import de.th.koeln.transaktionenservice.dto.FinanzdatenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransaktionenService {

    private final FinanzdatenGrpcClient client;

    @Autowired
    public TransaktionenService(FinanzdatenGrpcClient client) {
        this.client = client;
    }

    public List<FinanzdatenDto> getAlleFinanzdaten(Long kontoId){
        List<FinanzdatenDTO> einnahmenProto = this.client.getAllEinnahmen(kontoId);
        List<FinanzdatenDTO> ausgabenProto = this.client.getAllAusgaben(kontoId);

        List<FinanzdatenDto> einnahmen = einnahmenProto.stream()
                .map(FinanzdatenMapper::toDto)
                .toList();

        List<FinanzdatenDto> ausgaben = ausgabenProto.stream()
                .map(FinanzdatenMapper::toDto)
                .toList();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return Stream.concat(einnahmen.stream(), ausgaben.stream())
                .sorted(Comparator.comparing((FinanzdatenDto f) -> LocalDate.parse(f.getDatum(),formatter)).reversed() )
                .collect(Collectors.toCollection(ArrayList::new));

    }
}
