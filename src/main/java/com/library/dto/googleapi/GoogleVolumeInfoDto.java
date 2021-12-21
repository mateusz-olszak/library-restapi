package com.library.dto.googleapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoogleVolumeInfoDto {
    @JsonProperty("title")
    private String title;
    @JsonProperty("pageCount")
    private int pageCount;
    @JsonProperty("averageRating")
    private double averageRating;
    @JsonProperty("ratingsCount")
    private int ratingsCount;
    @JsonProperty("imageLinks")
    private GoogleImageLinksDto googleImageLinksDto;
}
