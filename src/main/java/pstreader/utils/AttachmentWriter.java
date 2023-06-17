package main.java.pstreader.utils;

import main.java.pstreader.entity.Attachment;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;

import static main.java.pstreader.StaticVars.MAX_ATTACHMENT_SIZE;

public class AttachmentWriter {

    FilesAndFoldersManagement filesAndFoldersManagement;
    public static final Logger logger = LogManager.getLogger(AttachmentWriter.class);
    public AttachmentWriter(FilesAndFoldersManagement filesAndFoldersManagement) {
        this.filesAndFoldersManagement = filesAndFoldersManagement;
    }

    public boolean createAttachment(String pathDir, Attachment attachment) throws IOException {
        boolean isAttachmentCreated = false;
        if(attachment.getContent().read() <= MAX_ATTACHMENT_SIZE){
            byte[] buf = new byte[MAX_ATTACHMENT_SIZE];
            logger.info("Path dir : "+pathDir+" - attachment : "+attachment.getName());
            filesAndFoldersManagement.createDir(pathDir);
            try (OutputStream outputStream = new FileOutputStream(pathDir + "/" + attachment.getName())) {
                int length;
                logger.info("Creating attachment file "+attachment.getName()+" ...");
                while ((length = attachment.getContent().read(buf)) != -1) {
                    outputStream.write(buf, 0, length);
                }
                isAttachmentCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            logger.error("attachment cannot be created, its size exceeds the maximum limit allowed");
        }
        return isAttachmentCreated;
    }
}
