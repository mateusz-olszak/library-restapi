package com.library.dto.currencies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.dto.currencies.CurrencyDto;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrencyExchangeDto {
    @JsonProperty("rates")
    private CurrencyDto currencies;
}
