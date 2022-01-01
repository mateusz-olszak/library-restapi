package com.library.unit.client;

import com.library.client.CurrencyExchangeClient;
import com.library.dto.currencies.CurrencyDto;
import com.library.dto.currencies.CurrencyExchangeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchaneClientUnitTestSuite {

    @InjectMocks
    @Spy
    private CurrencyExchangeClient currencyExchangeClient;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void testRetrieveCurrencyData() throws Exception {
        // Given
        String currenyFrom = "GBP";
        String currencyTo = "PLN";
        double amount = 1;

        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setPlnD(5.50);

        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto(currencyDto);

        URI url = UriComponentsBuilder.fromHttpUrl("https://api.exchangerate.host/latest")
                .queryParam("base",currenyFrom)
                .queryParam("symbols",currencyTo)
                .queryParam("amount",amount)
                .queryParam("places",2)
                .build().encode().toUri();

        when(restTemplate.getForObject(url,CurrencyExchangeDto.class)).thenReturn(currencyExchangeDto);
        // When
        CurrencyExchangeDto resultCurrency = currencyExchangeClient.retrieveCurrencyData(currenyFrom, currencyTo, amount);
        // Then
        assertEquals(5.50, resultCurrency.getCurrencies().getPlnD());
    }

}
