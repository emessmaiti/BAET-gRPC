package de.th.koeln.benachrichtigungservice.dtos;

public class BenutzerDTO {

    private String benutzerID;
    private String email;

    public BenutzerDTO () {}

    public String getBenutzerID() {
        return benutzerID;
    }

    public void setBenutzerID(String benutzerID) {
        this.benutzerID = benutzerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
