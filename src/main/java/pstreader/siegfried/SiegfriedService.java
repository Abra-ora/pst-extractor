package main.java.pstreader.siegfried;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.pstreader.entity.Attachment;
import main.java.pstreader.utils.AttachmentWriter;
import main.java.pstreader.utils.FilesAndFoldersManagement;
import okhttp3.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Base64;


public class SiegfriedService {

    public static final Logger logger = LogManager.getLogger(SiegfriedService.class);
    public static final String SIEGFRIED_HOST = "localhost";
    public static final int SIEGFRIED_PORT = 19000;
    public static final String SIEGFRIED_ACTION = "identify";
    private final Base64.Encoder base64 = Base64.getEncoder();
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    FilesAndFoldersManagement filesAndFoldersManagement;
    AttachmentWriter attachmentWriter;
    public SiegfriedService(FilesAndFoldersManagement filesAndFoldersManagement, AttachmentWriter attachmentWriter) {
        this.filesAndFoldersManagement = filesAndFoldersManagement;
        this.attachmentWriter = attachmentWriter;
    }

    private HttpUrl getSiegfriedUrl(Attachment attachment, String attachmentsDirPathParam) throws IOException {
        // create attachment file
        attachmentWriter.createAttachment(attachmentsDirPathParam, attachment);
        File attachmentsDir = new File(attachmentsDirPathParam);
        Path attachmentsDirPath = Path.of(attachmentsDir.getAbsolutePath());
        byte[] attachmentBytes = attachmentsDirPath.resolve(attachment.getName()).toString().getBytes();
        return new HttpUrl.Builder()
                .scheme("http")
                .host(SIEGFRIED_HOST)
                .port(19000)
                .addPathSegment(SIEGFRIED_ACTION)
                .addPathSegment(base64.encodeToString(attachmentBytes))
                .addQueryParameter("base64", "true")
                .addQueryParameter("format", "json")
                .build();
    }

    public Attachment postToSiegfried(Attachment attachment, String attachmentsDirPath) throws IOException {
        SiegfriedResponse siegfriedResponse = new SiegfriedResponse();
        try{
            HttpUrl url = getSiegfriedUrl(attachment, attachmentsDirPath);
            Request request = new Request.Builder()
                    .get()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (!response.isSuccessful() || body == null) {
                logger.error("Error response body empty or response not successful (url :"+url.toString()+").");
            }
            assert body != null;
            siegfriedResponse = mapper.readValue(body.bytes(), SiegfriedResponse.class);
        }catch (IOException e ) {
            e.printStackTrace();
        }
        return Attachment.siegfriedMapper(siegfriedResponse);
    }

    public byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
        try {
            byte[] array = inputStream.readAllBytes();
            inputStream.close();
            return array;
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        return new byte[0];
    }

}
