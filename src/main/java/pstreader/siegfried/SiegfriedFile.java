package main.java.pstreader.siegfried;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiegfriedFile {

    @JsonProperty("filename")
    private String name;
    @JsonProperty("filesize")
    private long size;
    @JsonProperty("matches")
    private List<SiegfriedMatch> matches;

    public SiegfriedFile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<SiegfriedMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<SiegfriedMatch> matches) {
        this.matches = matches;
    }
}
