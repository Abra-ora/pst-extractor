package main.java.pstReader.entity;

import com.pff.PSTException;
import com.pff.PSTMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Email {
    private String sender;
    private List<String> receivers = new ArrayList<>();
    private String subject;
    private String body;
    private Date clientSubmitDate;
    private boolean hasAttachment;
    private List<Attachment> attachments = new ArrayList<>();

    public Email(String sender, List<String> receivers, String subject, String body, Date clientSubmitDate,
                 List<Attachment> attachments) {
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.body = body;
        this.clientSubmitDate = clientSubmitDate;
        this.attachments = attachments;
    }

    public static Email emailMapper(PSTMessage pstMessage){
        return new Email(
                pstMessage.getSenderEmailAddress(),
                gerReceiversFromPSTMessage(pstMessage),
                pstMessage.getSubject(),
                pstMessage.getBody(),
                pstMessage.getClientSubmitTime(),
                getAttachmentFromPSTMessage(pstMessage)
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
                   extractedAttachment.add(Attachment.attachmentMapper(pstMessage.getAttachment(i)));
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
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public void setClientSubmitDate(Date clientSubmitDate) {
        this.clientSubmitDate = clientSubmitDate;
    }
}
