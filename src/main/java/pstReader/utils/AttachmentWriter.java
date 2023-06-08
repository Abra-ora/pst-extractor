package main.java.pstReader.utils;

import main.java.pstReader.entity.Attachment;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;

public class AttachmentWriter {

    FilesAndFoldersManagement filesAndFoldersManagement;
    public static final Logger logger = LogManager.getLogger(AttachmentWriter.class);
    public AttachmentWriter(FilesAndFoldersManagement filesAndFoldersManagement) {
        this.filesAndFoldersManagement = filesAndFoldersManagement;
    }

    public static void createAttachment(String pathDir, Attachment attachment) throws IOException {
        byte[] buf = new byte[8192];
        try (OutputStream outputStream = new FileOutputStream(pathDir + "/" + attachment.getName())) {
            int length;
//            logger.info("Creating attachment file "+attachment.getName()+" ...");
            while ((length = attachment.getContent().read(buf)) != -1) {
                outputStream.write(buf, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
