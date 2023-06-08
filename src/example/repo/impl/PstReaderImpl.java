package example.repo.impl;

import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import example.entity.Email;
import example.repo.PstReaderRepo;
import example.utils.FilesAndFoldersManagement;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PstReaderImpl implements PstReaderRepo {

    FilesAndFoldersManagement filesAndFoldersManagement;
    public static String OUTPUTDIR = "./output";
    List<String> pathLevels = new ArrayList<>();

    public static final Logger logger = LogManager.getLogger(PstReaderImpl.class);
    public PstReaderImpl(FilesAndFoldersManagement filesAndFoldersManagement) {
        this.filesAndFoldersManagement = filesAndFoldersManagement;
    }

    public PSTFolder readPST(String pstFilePath){
//        logger.info("Reading PST file ...");
        try{
            PSTFile pstFile = new PSTFile(pstFilePath);
            return pstFile.getRootFolder();
        }catch (PSTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTreeStructure(String pstFilePath) {
        try{
//            logger.info("Reading PST file ...");
            PSTFile pstFile = new PSTFile(pstFilePath);
            PSTFolder rootFolder = pstFile.getRootFolder();
            pathLevels.add(OUTPUTDIR);
            pathLevels.add(pstFile.getMessageStore().getDisplayName());
            createSubTree(rootFolder, pathLevels);
//            logger.info("Check output directory to see the result");
        } catch (PSTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createSubTree(PSTFolder pstFolder, List<String> pathLevels) {
        try{
            String tmpPath = filesAndFoldersManagement.createPathFromList(pathLevels);
            filesAndFoldersManagement.createDir(tmpPath);
            if(pstFolder.getContentCount() > 0){
                createEmails(pstFolder, tmpPath);
            }
            Vector<PSTFolder> subFolders = pstFolder.getSubFolders();
            for (int i = 0; i < subFolders.size() ; i++) {
                pathLevels.add(subFolders.get(i).getDisplayName());
                tmpPath = filesAndFoldersManagement.createPathFromList(pathLevels);
                filesAndFoldersManagement.createDir(tmpPath);
                if(subFolders.get(i).getContentCount() > 0){
                    createEmails(subFolders.get(i), tmpPath);
                }
                if(subFolders.get(i).hasSubfolders()){
                    createSubTree(subFolders.get(i), pathLevels);
                }
                pathLevels.remove(pathLevels.size()-1);
            }
        } catch (PSTException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createEmails(PSTFolder pstFolder, String dirName) {
        if(pstFolder.getContentCount() > 0){
           try{
               PSTMessage email = (PSTMessage) pstFolder.getNextChild();
               while (email != null){
                   Email mappedEmail = Email.emailMapper(email);
                   if (email.hasAttachments()) {
                       String emailDir = dirName +"/"+email.getSubject()+"-folder";
                       filesAndFoldersManagement.createDir(emailDir);
                       filesAndFoldersManagement.crateEmailJson(mappedEmail, emailDir);
                   }
                   email = (PSTMessage) pstFolder.getNextChild();
               }
           } catch (PSTException | IOException e) {
               throw new RuntimeException(e);
           }

        }
    }
}
