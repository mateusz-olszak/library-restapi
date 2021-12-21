package com.library.dto.currencies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrencyDto {
    @JsonProperty("EUR")
    private double euro;
    @JsonProperty("USD")
    private double usd;
    @JsonProperty("PLN")
    private double plnD;
    @JsonProperty("GBP")
    private double gbp;
    @JsonProperty("MXN")
    private double mxn;
}
