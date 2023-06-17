package main.java.pstreader.siegfried;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SiegfriedResponse {

    @JsonProperty("files")
    private List<SiegfriedFile> files;

    public SiegfriedResponse() {
    }

    public List<SiegfriedFile> getFiles() {
        return files;
    }

    public void setFiles(List<SiegfriedFile> files) {
        this.files = files;
    }
}
