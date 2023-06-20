package main.java.pstreader;

import main.java.pstreader.siegfried.SiegfriedService;
import main.java.pstreader.utils.AttachmentWriter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import main.java.pstreader.repo.PstReaderRepo;
import main.java.pstreader.repo.impl.PstReaderImpl;
import main.java.pstreader.utils.FilesAndFoldersManagement;
import java.io.IOException;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args){
        BasicConfigurator.configure();
        FilesAndFoldersManagement filesAndFoldersManagement = new FilesAndFoldersManagement();
        AttachmentWriter attachmentWriter = new AttachmentWriter(filesAndFoldersManagement);
        SiegfriedService siegfriedService = new SiegfriedService(filesAndFoldersManagement, attachmentWriter);
        PstReaderRepo pstReaderRepo = new PstReaderImpl(filesAndFoldersManagement, siegfriedService);

        filesAndFoldersManagement.checkIfPathIsValid(args[0]);
        if(args[1] != null) FilesAndFoldersManagement.checkIfDir(args[1]);

        pstReaderRepo.createTreeStructure(args[0]);

        // PST 1:  /home/ibtal/projects/pst/pst-files/user1@test.lab.pst
        // PST 2:  /home/ibtal/projects/pst/pst-files/sample.pst
        // PST 3:  /home/ibtal/projects/pst/pst-files/support.pst
        // PST4 :  /home/ibtal/projects/pst/pst-files/enron.pst
    }



}