package main.java.pstreader.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pff.PSTAttachment;
import com.pff.PSTException;
import main.java.pstreader.siegfried.SiegfriedFile;
import main.java.pstreader.siegfried.SiegfriedMatch;
import main.java.pstreader.siegfried.SiegfriedResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {

    public static final Logger logger = LogManager.getLogger(Attachment.class);

    @JsonIgnore
    private InputStream content;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Format")
    private String format;
    @JsonProperty("Mime")
    private String mime;
    @JsonProperty("version")
    private String version;
    @JsonProperty("Size")
    private long size;

    @JsonProperty("warning")
    private String warning;

    public Attachment() {
    }

    public static Attachment simpleMapper(PSTAttachment pstAttachment){
        Attachment attachment = new Attachment();
        try{
            if(pstAttachment.getFilename().isEmpty()){
                attachment.setName("Embedded_attachment");
            }else {
                attachment.setName(pstAttachment.getFilename());
            }
            attachment.setContent(pstAttachment.getFileInputStream());
        }catch (PSTException | IOException e){
            e.printStackTrace();
        }
        return attachment;
    }
    public static Attachment siegfriedMapper(SiegfriedResponse siegfriedResponse){
        Attachment attachment = new Attachment();
        try {
            SiegfriedFile siegfriedFile = siegfriedResponse.getFiles().get(0);
            attachment.setName(siegfriedFile.getName());
            attachment.setSize(siegfriedFile.getSize());
            if(siegfriedFile.getMatches().size() > 1){
                logger.info("Attachment has multiple matches");
            }
            SiegfriedMatch siegfriedMatch = siegfriedFile.getMatches().get(0);
            attachment.setId(siegfriedMatch.getId());
            attachment.setFormat(siegfriedMatch.getFormat());
            attachment.setMime(siegfriedMatch.getMime());
            attachment.setVersion(siegfriedMatch.getVersion());
            attachment.setWarning(siegfriedMatch.getWarning());
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return attachment;
    }

    public String getName() {
        return name;
    }

    public InputStream getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }


    @Override
    public String toString() {
        return "Attachment{" +
                "content=" + content +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", mime='" + mime + '\'' +
                ", version='" + version + '\'' +
                ", size=" + size +
                ", warning=" + warning +
                '}';
    }
}
