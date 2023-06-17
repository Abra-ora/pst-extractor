package main.java.pstreader.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractedMetadata {

    @JsonProperty("Pst FileName")
    private String pstFileName;

    @JsonProperty("Emails")
    private List<Email> emails = new ArrayList<>();

    public ExtractedMetadata() {
    }

    public ExtractedMetadata(String pstFileName, List<Email> emails) {
        this.pstFileName = pstFileName;
        this.emails = emails;
    }

    public void addEmail(Email email, String emailPath){
        email.setId(emails.size()+1);
        email.setPath(emailPath);
        emails.add(email);
    }

    public String getPstFileName() {
        return pstFileName;
    }

    public void setPstFileName(String pstFileName) {
        this.pstFileName = pstFileName;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
}
