package main.java.pstreader.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.pstreader.entity.ExtractedMetadata;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class JsonMapper {

    public static final Logger logger = LogManager.getLogger(JsonMapper.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();


    public static void jsonWriter(ExtractedMetadata extractedMetadata, String filePath){
        try {

                logger.info("JSON MAPPER Email Path ==> "+filePath);
                jsonMapper.writer().writeValue(new File(filePath), extractedMetadata);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
