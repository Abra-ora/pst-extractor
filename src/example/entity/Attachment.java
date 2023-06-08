package example.entity;

import com.pff.PSTAttachment;
import com.pff.PSTException;

import java.io.IOException;
import java.io.InputStream;

public class Attachment {

    private String name;
    private InputStream content;
    private String type;
    private int size;

    public Attachment(String name, InputStream content, String type, int size) {
        this.name = name;
        this.content = content;
        this.type = type;
        this.size = size;
    }

    public static Attachment attachmentMapper(PSTAttachment pstAttachment){
        String fileName;
        String type;
        if(!pstAttachment.getFilename().isEmpty()){
            String [] fileNameSplit = pstAttachment.getFilename().split("\\.");
            fileName = pstAttachment.getFilename();
            type = fileNameSplit[fileNameSplit.length-1];
        }else{
            fileName = "NOT_IDENTIFIED";
            type = "NOT_IDENTIFIED";
        }
        try {
            return new Attachment(
                    fileName,
                    (InputStream) pstAttachment.getFileInputStream(),
                    type,
                    pstAttachment.getSize()
                    );
        } catch (PSTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public InputStream getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public void setType(String contentType) {
        this.type = contentType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
