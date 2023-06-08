package example.repo;

import com.pff.PSTFolder;
import com.pff.PSTMessage;

import java.util.List;

public interface PstReaderRepo {

    public PSTFolder readPST(String pstFilePath);
    public void createTreeStructure(String pstFilePath);
    public void createEmails(PSTFolder pstFolder, String dirName);

    public void createSubTree(PSTFolder pstFolder, List<String> pathLevels);
}
