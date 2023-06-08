package example.utils;


import example.entity.Attachment;
import example.entity.Email;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FilesAndFoldersManagement {

    private static final Logger logger =
            LogManager.getLogger(FilesAndFoldersManagement.class);

    public void createDir(String dirPath){
        try{
            File directory = new File(dirPath);
            if(!directory.exists()){
//                logger.info("Creating directory with path "+ dirPath+" ...");
                directory.mkdirs();
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
//            logger.info("The directory with path "+dirPath+"is deleted");
        }catch (IOException e){
            throw new IOException(e);
        }
    }
    public void crateEmailJson(Email email, String dirPath){
        File emailFile = new File(dirPath, email.getSubject()+".json");
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        if(!emailFile.exists()){
            try{
                JSONObject emailJson = new JSONObject();
                emailJson.put("Sender", email.getSender());
                emailJson.put("Subject", email.getSubject());
                emailJson.put("Submit date", dateFormat.format(email.getClientSubmitDate()));
                emailJson.put("Receiver", email.getReceivers());
                emailJson.put("Email content", email.getBody());

                if(!email.getAttachments().isEmpty()){
                    emailJson.put("Attachments", createJsonAttachments(email, dirPath));
                }
//                logger.info("Creating "+email.getSubject()+".json file ...");
                writeFile(email.getSubject() , dirPath, emailJson);
            } catch (NullPointerException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<JSONObject> createJsonAttachments(Email email, String dirPath) {
         List<JSONObject> jsonAttachmentsList = new ArrayList<>();
         JSONObject jsonAttachmentTmp = new JSONObject();
         try{
             Attachment tmpAttachment;
             for (int i = 0; i < email.getAttachments().size(); i++) {
                 tmpAttachment = email.getAttachments().get(i);
                 jsonAttachmentTmp.put("name", tmpAttachment.getName());
                 jsonAttachmentTmp.put("type", tmpAttachment.getType());
                 jsonAttachmentsList.add((JSONObject) jsonAttachmentTmp.clone());
                 AttachmentWriter.createAttachment(dirPath, tmpAttachment);
                 jsonAttachmentTmp.clear();
             }
             return jsonAttachmentsList;
         }
         catch (Exception e) {
             throw new RuntimeException(e);
         }
    }

    private static void writeFile(String fileName, String dirPath, JSONObject jsonObject) throws IOException {
        try (FileWriter fileWriter = new FileWriter(dirPath + "/" + fileName + ".json")) {
            fileWriter.write(jsonObject.toJSONString());
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public String createPathFromList(List<String> pathLevels){
        StringBuilder pathAbs = new StringBuilder();
        for(String path : pathLevels){
            pathAbs.append(path);
            pathAbs.append("/");
        }
//        logger.info("New path levels generated: "+ pathLevels);
        return pathAbs.toString();
    }

    public boolean checkIfPathIsValid(String path){
        File file = new File(path);
        if(file.exists()){
            return true;
        }else{
            System.out.println( "=> The path is not valid.");
            return false;
        }
    }
}
