package com.library.dto.googleapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoogleItemDto {
    @JsonProperty("volumeInfo")
    private GoogleVolumeInfoDto googleVolumeInfoDtos;
}
