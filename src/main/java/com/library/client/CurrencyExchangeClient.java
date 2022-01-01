package com.library.client;

import com.library.dto.currencies.CurrencyExchangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class CurrencyExchangeClient {

    private final RestTemplate restTemplate;

    public CurrencyExchangeDto retrieveCurrencyData(String base, String convertTo, double amount) {
        URI url = buildUrl(base,convertTo,amount);

        CurrencyExchangeDto response = restTemplate.getForObject(
            url, CurrencyExchangeDto.class
        );
        return response;
    }

    private URI buildUrl(String base, String convertTo, double amount) {
        URI url = UriComponentsBuilder.fromHttpUrl("https://api.exchangerate.host/latest")
                .queryParam("base",base)
                .queryParam("symbols",convertTo)
                .queryParam("amount",amount)
                .queryParam("places",2)
                .build().encode().toUri();
        return url;
    }
}
