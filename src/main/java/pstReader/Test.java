package main.java.pstReader;

import com.pff.*;
import org.apache.log4j.BasicConfigurator;

import java.util.*;

public class Test {
    public static void main(String[] args)
    {
        BasicConfigurator.configure();
        new Test("/home/ibtal/projects/pst/pst-files/user1@test.lab.pst");
//        new Test("/home/ibtal/projects/pst/pst-files/sample.pst");

//        FilesAndFoldersManagement filesAndFoldersManagement = new FilesAndFoldersManagement();
//        filesAndFoldersManagement.createDir("./result//output", "testDir");
    }

    public Test(String filename) {
        try {
            PSTFile pstFile = new PSTFile(filename);
            System.out.println(pstFile.getMessageStore().getDisplayName());
            processFolder(pstFile.getRootFolder());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    int depth = -1;
    public void processFolder(PSTFolder folder)
            throws PSTException, java.io.IOException
    {
        depth++;
        // the root folder doesn't have a display name
        if (depth > 0) {
            printDepth();
            System.out.println(folder.getDisplayName());
        }

        // go through the folders...
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processFolder(childFolder);
            }
        }

        // and now the emails for this folder
        if (folder.getContentCount() > 0) {
            depth++;
            PSTMessage email = (PSTMessage)folder.getNextChild();
            while (email != null) {
                printDepth();
                System.out.println("Email: "+email.getSubject());
//                if(email.getNumberOfRecipients() > 0){
//                    for (int i = 0; i < email.getNumberOfRecipients() ; i++) {
//                        printDepth();
//                        System.out.println("Receiver: "+ email.getRecipient(i).getEmailAddress());
//                    }
//                }
//                System.out.println("Email content: "+ email.getBody());
//                System.out.println("ClientSubmitTime: "+ email.getClientSubmitTime());
////                System.out.println("ReceivedByName: "+email.getReceivedByName());
                if(email.hasAttachments()){
                    for (int i = 0; i < email.getNumberOfAttachments(); i++) {
//                        System.out.println("Attachement display name: "+ email.getAttachment(i).getDisplayName());
                        System.out.println("Attachement file name: "+ email.getAttachment(i).getFilename());
//                        System.out.println("Attachement size: "+ email.getAttachment(i).getSize());
//                        System.out.println("Attachement file size: "+ email.getAttachment(i).getFilesize());
//                        System.out.println("Attachement email address: "+ email.getAttachment(i).getEmailAddress());
                        System.out.println("Attachement embedded pst message: "
                                + email.getAttachment(i).getFileInputStream().read());
                    }
                }
                email = (PSTMessage)folder.getNextChild();
            }
            depth--;
        }
        depth--;
    }

    public void printDepth() {
        for (int x = 0; x < depth-1; x++) {
            System.out.print(" | ");
        }
        System.out.print(" |- ");
    }
}