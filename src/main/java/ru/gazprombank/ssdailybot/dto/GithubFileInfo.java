package ru.gazprombank.ssdailybot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubFileInfo {

    @JsonProperty("download_url")
    private String downloadUrl;

}
