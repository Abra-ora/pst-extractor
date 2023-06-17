    package main.java.pstreader.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pff.PSTException;
import com.pff.PSTMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Email {

    @JsonProperty("Id")
    private long id;
    @JsonProperty("Sender")
    private String sender;
    @JsonProperty("Receivers")
    private List<String> receivers = new ArrayList<>();
    @JsonProperty("Subject")
    private String subject;
    @JsonProperty("Email content")
    private String body;
    @JsonProperty("Email path")
    private String path;
    @JsonProperty("Submit date")
    private Date clientSubmitDate;
    @JsonIgnore
    private boolean hasAttachment;
    @JsonProperty("Email attachments")
    private List<Attachment> attachments = new ArrayList<>();

    public Email(String sender, List<String> receivers, String subject, String body, Date clientSubmitDate,
                 List<Attachment> attachments, boolean hasAttachment) {
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.body = body;
        this.clientSubmitDate = clientSubmitDate;
        this.attachments = attachments;
        this.hasAttachment = hasAttachment;
    }

    public static Email emailMapper(PSTMessage pstMessage){
        return new Email(
                pstMessage.getSenderEmailAddress(),
                gerReceiversFromPSTMessage(pstMessage),
                pstMessage.getSubject(),
                pstMessage.getBody().isEmpty() ?
                        pstMessage.getBodyHTML() : pstMessage.getBody(),
                pstMessage.getClientSubmitTime(),
                getAttachmentFromPSTMessage(pstMessage),
                pstMessage.hasAttachments()
                );
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public static List<String> gerReceiversFromPSTMessage(PSTMessage pstMessage){
        List<String> extractedReceivers = new ArrayList<>();
        try{
            if(pstMessage.getNumberOfRecipients() > 0){
                for (int i = 0; i <pstMessage.getNumberOfRecipients() ; i++) {
                    extractedReceivers.add(pstMessage.getRecipient(i).getEmailAddress());
                }
            }
            return extractedReceivers;
        } catch (PSTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static List<Attachment> getAttachmentFromPSTMessage(PSTMessage pstMessage){
        List<Attachment> extractedAttachment = new ArrayList<>();
        try{
           if(pstMessage.hasAttachments()){
               for (int i = 0; i < pstMessage.getNumberOfAttachments() ; i++) {
                   extractedAttachment.add(Attachment.simpleMapper(pstMessage.getAttachment(i)));
               }
           }
           return extractedAttachment;
       } catch (PSTException | IOException e) {
           throw new RuntimeException(e);
       }
    }
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Date getClientSubmitDate() {
        return clientSubmitDate;
    }

    public boolean isHasAttachment() {
        return this.hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public void setClientSubmitDate(Date clientSubmitDate) {
        this.clientSubmitDate = clientSubmitDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receivers=" + receivers +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", path='" + path + '\'' +
                ", clientSubmitDate=" + clientSubmitDate +
                ", hasAttachment=" + hasAttachment +
                ", attachments=" + attachments +
                '}';
    }
}
