package com.library.dto.googleapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoogleBooksRatingDto {
    @JsonProperty("items")
    private GoogleItemDto[] items;
}
