package main.java.pstReader.ui;

import main.java.pstReader.repo.PstReaderRepo;
import main.java.pstReader.repo.impl.PstReaderImpl;
import main.java.pstReader.utils.FilesAndFoldersManagement;

import java.io.IOException;
import java.util.Scanner;

public class TerminalMenu {

    FilesAndFoldersManagement filesAndFoldersManagement = new FilesAndFoldersManagement();
    PstReaderRepo pstReaderRepo = new PstReaderImpl(filesAndFoldersManagement);

    public TerminalMenu(FilesAndFoldersManagement filesAndFoldersManagement, PstReaderRepo pstReaderRepo) {
        this.filesAndFoldersManagement = filesAndFoldersManagement;
        this.pstReaderRepo = pstReaderRepo;
    }

    public void launch() throws IOException, InterruptedException {
        int choice;
        Scanner sc = new Scanner(System.in);
        while(true){
            menu();
            System.out.print("Enter your choice:  ");
            choice = sc.nextInt();
            execute(choice);
        }
    }

    public void execute(int choice) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        boolean pathIsValid = false;
        String pstPath = null;
        switch (choice){
            case 1:
                while (!pathIsValid){
                    System.out.print("\nEnter pst file path:  ");
                    pstPath = sc.nextLine();
                    pathIsValid = filesAndFoldersManagement.checkIfPathIsValid(pstPath);
                }
                pstReaderRepo.createTreeStructure(pstPath);
                System.out.println("==> Check ./out folder to see the extracted files");
                break;
            case 2:
                filesAndFoldersManagement.deleteDir(PstReaderImpl.OUTPUTDIR);
                break;
            case 3:
                System.exit(0);
                break;
            default:
        }
    }
    public void menu(){
        System.out.println("\n\n=========== PST EXTRACTOR ===========");
        System.out.println("    => 1. Choose pst file to extract");
        System.out.println("    => 2. Delete results directory");
        System.out.println("    => 3. Exit\n\n");
    }

}
