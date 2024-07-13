package de.th.koeln.benachrichtigungservice.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ZieleDTO {

    private String benutzerId;
    private BigDecimal sparbetrag;
    private LocalDate faelligkeitDatum;
    private String zielName;

    public ZieleDTO(){}

    public String getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(String benutzerId) {
        this.benutzerId = benutzerId;
    }

    public BigDecimal getSparbetrag() {
        return sparbetrag;
    }

    public void setSparbetrag(BigDecimal sparbetrag) {
        this.sparbetrag = sparbetrag;
    }

    public LocalDate getFaelligkeitDatum() {
        return faelligkeitDatum;
    }

    public void setFaelligkeitDatum(LocalDate faelligkeitDatum) {
        this.faelligkeitDatum = faelligkeitDatum;
    }

    public String getZielName() {
        return zielName;
    }

    public void setZielName(String zielName) {
        this.zielName = zielName;
    }
}
