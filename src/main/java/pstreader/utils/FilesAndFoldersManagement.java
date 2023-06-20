package main.java.pstreader.utils;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class FilesAndFoldersManagement {

    public static String outputDir;
    public static String attachmentsDir = "/attachments/";
    private static final Logger logger =
            LogManager.getLogger(FilesAndFoldersManagement.class);

    public void createDir(String dirPath){
        try{
            File directory = new File(dirPath);
            if(!directory.exists()){
                directory.mkdirs();
//                logger.info("Directory created : "+dirPath);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteDir(String dirPath) throws IOException {
        try(Stream<Path> path = Files.walk(Paths.get(dirPath))) {
            path.sorted((a, b) -> b.toString().length() - a.toString().length())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }catch (IOException e){
            throw new IOException(e);
        }
    }

    private boolean createFile(String fileName, String dirPath) {
        try{
            createDir(dirPath);
            File file = new File(dirPath+fileName);
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String createPathFromList(List<String> pathLevels){
        StringBuilder pathAbs = new StringBuilder();
        for(String path : pathLevels){
            pathAbs.append(path);
            pathAbs.append("/");
        }
        return pathAbs.toString();
    }

    public void checkIfPathIsValid(String path) {
        File file = new File(path);
          try {
            if (!file.exists()) {
                logger.error("=> The path is not correct or the file was not found!");
                throw new NoSuchFileException("No such file");
            }
        }catch (NoSuchFileException e){
              e.printStackTrace();
        }
    }

    public static void checkIfDir(String path) {
        File file = new File(path);
        if (file.isDirectory()){
            outputDir = path+"/";
        }else{
            outputDir = "./";
            logger.error("No such directory, check ./ to see the results");
        }
    }
}
