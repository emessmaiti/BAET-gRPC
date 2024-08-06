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

/**
 * Service-Klasse für Benachrichtigungen.
 *
 * Diese Klasse verwaltet die Benachrichtigungsvorgänge, einschließlich des Abrufens von
 * Benutzerdaten und finanziellen Zielen sowie dem Senden von Erinnerungs-E-Mails.
 */
@Service
public class BenachrichtigungService {

    private final BenutzerGrpcClient benutzerClient;
    private final ZieleGrpClient zieleClient;
    private final JavaMailSender mailSender;

    private final static String SUBJECT = "Benachrichtigung";
    private final static String TEXT = "Please remember to set your budget for your financial goal: ";

    /**
     * Konstruktor für BenachrichtigungService.
     *
     * @param benutzerClient Der gRPC-Client für Benutzerdienste.
     * @param zieleClient Der gRPC-Client für Finanzdaten-Dienste.
     * @param mailSender Der Dienst zum Senden von E-Mails.
     */
    @Autowired
    public BenachrichtigungService(BenutzerGrpcClient benutzerClient, ZieleGrpClient zieleClient, JavaMailSender mailSender) {
        this.benutzerClient = benutzerClient;
        this.zieleClient = zieleClient;
        this.mailSender = mailSender;
    }

    /**
     * Findet Benutzerdaten basierend auf der Benutzer-ID.
     *
     * @param benutzerId Die ID des Benutzers.
     * @return Das BenutzerDTO mit den Benutzerdaten.
     */
    public BenutzerDTO findBenutzerById(String benutzerId) {
        return BenutzerdatenMapper.toDto(this.benutzerClient.findBenutzerById(benutzerId));
    }

    /**
     * Ruft alle finanziellen Ziele ab.
     *
     * @return Eine Liste von ZieleDTO mit den finanziellen Zielen.
     */
    public List<ZieleDTO> getAlleZiel() {
        List<FinanzdatenDTO> zieleProto = this.zieleClient.getZiele();
        return zieleProto.stream()
                .map(ZieleMapper::toDto)
                .toList();
    }

    /**
     * Benachrichtigt Benutzer täglich um 9 Uhr.
     *
     * Diese Methode wird täglich um 9 Uhr ausgeführt und sendet Erinnerungs-E-Mails
     * an Benutzer, deren finanzielle Ziele innerhalb der nächsten 7 Tage fällig werden.
     */
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

    /**
     * Sendet eine E-Mail.
     *
     * Diese Methode sendet eine E-Mail an die angegebene Adresse mit dem angegebenen Text.
     *
     * @param to Die E-Mail-Adresse des Empfängers.
     * @param text Der Text der E-Mail.
     */
    private void sendMail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText(text);
        mailSender.send(message);
    }

}
