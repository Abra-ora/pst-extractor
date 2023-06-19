package main.java.pstreader.repo;

import com.pff.PSTFolder;
import main.java.pstreader.entity.ExtractedMetadata;

import java.util.List;

public interface PstReaderRepo {

    public PSTFolder readPST(String pstFilePath);
    public void createTreeStructure(String pstFilePath);
    public void createEmails(PSTFolder pstFolder, ExtractedMetadata extractedMetadata, String dirName);
    public void createSubTree(PSTFolder pstFolder, ExtractedMetadata extractedMetadata, List<String> pathLevels);
    public void identifyAndCreateAttachments(ExtractedMetadata extractedMetadata);
}
