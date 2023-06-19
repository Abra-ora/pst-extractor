package main.java.pstreader.repo.impl;

import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import main.java.pstreader.entity.Email;
import main.java.pstreader.entity.ExtractedMetadata;
import main.java.pstreader.repo.PstReaderRepo;
import main.java.pstreader.siegfried.SiegfriedService;
import main.java.pstreader.utils.AttachmentWriter;
import main.java.pstreader.utils.FilesAndFoldersManagement;
import main.java.pstreader.utils.JsonMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class PstReaderImpl implements PstReaderRepo {

    FilesAndFoldersManagement filesAndFoldersManagement;
    AttachmentWriter attachmentWriter;
    SiegfriedService siegfriedService;
    List<String> pathLevels = new ArrayList<>();

    public static final Logger logger = LogManager.getLogger(PstReaderImpl.class);
    public PstReaderImpl(FilesAndFoldersManagement filesAndFoldersManagement, SiegfriedService siegfriedService) {
        this.filesAndFoldersManagement = filesAndFoldersManagement;
        this.siegfriedService = siegfriedService;
    }

    public PSTFolder readPST(String pstFilePath){
        try{
            PSTFile pstFile = new PSTFile(pstFilePath);
            return pstFile.getRootFolder();
        }catch (PSTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTreeStructure(String pstFilePath) {
        ExtractedMetadata extractedMetadata = new ExtractedMetadata();
        try{
            PSTFile pstFile = new PSTFile(pstFilePath);
            PSTFolder rootFolder = pstFile.getRootFolder();

            String pstFileName = new File(pstFilePath).getName();
            String outDir = FilesAndFoldersManagement.outputDir+pstFileName+"/";
            String attachmentsDir = outDir+ FilesAndFoldersManagement.attachmentsDir;

            extractedMetadata.setPstFileName(pstFile.getMessageStore().getDisplayName());
            pathLevels.add(pstFile.getMessageStore().getDisplayName());

            createSubTree(rootFolder, extractedMetadata, pathLevels);
            identifyAndCreateAttachments(extractedMetadata, attachmentsDir);
            JsonMapper.jsonWriter(extractedMetadata, outDir+pstFileName+".json");
        } catch (PSTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void createSubTree(PSTFolder pstFolder,ExtractedMetadata extractedMetadata, List<String> pathLevels) {
        try{
            String emailPath;
            emailPath =filesAndFoldersManagement.createPathFromList(pathLevels);
            createEmails(pstFolder, extractedMetadata, emailPath);

            if(pstFolder.hasSubfolders()){
                Vector<PSTFolder> subFolders = pstFolder.getSubFolders();
                for (PSTFolder subFolder : subFolders){
                    pathLevels.add(subFolder.getDisplayName());
                    createSubTree(subFolder,extractedMetadata, pathLevels);
                    pathLevels.remove(pathLevels.size()-1);
                }
            }
        } catch (PSTException | IOException e) {
            e.printStackTrace();
        }
    }

    public void identifyAndCreateAttachments(ExtractedMetadata extractedMetadata, String attachmentsDirPath){
        for(Email email : extractedMetadata.getEmails()){
            if(email.isHasAttachment()){
                email.getAttachments()
                        .replaceAll(attachment -> {
                            try {
                                attachment = siegfriedService.postToSiegfried(attachment,attachmentsDirPath);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return attachment;
                        });
            }
        }
    }

    @Override
    public void createEmails(PSTFolder pstFolder, ExtractedMetadata extractedMetadata, String dirName){
        if(pstFolder.getContentCount() > 0){
            try {
                PSTMessage pstMessage = (PSTMessage) pstFolder.getNextChild();
                while(pstMessage != null){
                    Email email = Email.emailMapper(pstMessage);
                    String emailJsonFileName = dirName+email.getSubject().trim();
                    extractedMetadata.addEmail(email, emailJsonFileName);
                    pstMessage = (PSTMessage) pstFolder.getNextChild();
                }
            }catch (PSTException | IOException e) {
                   e.printStackTrace();
               }
            }
        }
}