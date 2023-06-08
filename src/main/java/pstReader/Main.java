package main.java.pstReader;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import main.java.pstReader.repo.PstReaderRepo;
import main.java.pstReader.repo.impl.PstReaderImpl;
import main.java.pstReader.ui.TerminalMenu;
import main.java.pstReader.utils.FilesAndFoldersManagement;
import java.io.IOException;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        BasicConfigurator.configure();
        FilesAndFoldersManagement filesAndFoldersManagement = new FilesAndFoldersManagement();
        PstReaderRepo pstReaderRepo = new PstReaderImpl(filesAndFoldersManagement);

        TerminalMenu terminalMenu = new TerminalMenu(filesAndFoldersManagement, pstReaderRepo);

        // PST 1:  /home/ibtal/projects/pst/pst-files/user1@test.lab.pst
        // PST 2:  /home/ibtal/projects/pst/pst-files/sample.pst
        terminalMenu.launch();

    }


}