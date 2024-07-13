package de.th.koeln.benachrichtigungservice.services;

import de.th.koeln.benachrichtigungservice.clients.BenutzerGrpcClient;
import de.th.koeln.benachrichtigungservice.clients.ZieleGrpClient;
import de.th.koeln.benachrichtigungservice.dtos.BenutzerDTO;
import de.th.koeln.benachrichtigungservice.dtos.BenutzerdatenMapper;
import de.th.koeln.benachrichtigungservice.dtos.ZieleDTO;
import de.th.koeln.benachrichtigungservice.dtos.ZieleMapper;
import de.th.koeln.finanzdaten.grpc.FinanzdatenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class BenachrichtigungService {

    private final BenutzerGrpcClient benutzerClient;
    private final ZieleGrpClient zieleClient;
    private final static String SUBJECT = "Benachrichtigung";
    private final static String TEXT = "Please remember to set your budget for your financial goal: ";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public BenachrichtigungService(BenutzerGrpcClient benutzerClient, ZieleGrpClient zieleClient) {
        this.benutzerClient = benutzerClient;
        this.zieleClient = zieleClient;
    }

    public BenutzerDTO findBenutzerById(String benutzerId) {
        return BenutzerdatenMapper.toDto(this.benutzerClient.findBenutzerById(benutzerId));
    }

    public List<ZieleDTO> getAlleZiel() {
        List<FinanzdatenDTO> zieleProto = this.zieleClient.getZiele();
        return zieleProto.stream()
                .map(ZieleMapper::toDto)
                .toList();
    }

    @Scheduled(cron = "0 0 9 * * ?") // Täglich um 9 Uhr
    public void benachrichtige() {
        LocalDate heute = LocalDate.now();
        List<ZieleDTO> ziele = getAlleZiel();

        for (ZieleDTO ziel : ziele) {
            if(ziel.getFaelligkeitDatum().isBefore((heute.plusDays(7)))) {
                BenutzerDTO benutzerDTO = findBenutzerById(ziel.getBenutzerId());
                this.sendMail(benutzerDTO.getEmail(), TEXT.concat(ziel.getZielName()) );
            }
        }
    }

    private void sendMail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText(text);
        mailSender.send(message);
    }

}
