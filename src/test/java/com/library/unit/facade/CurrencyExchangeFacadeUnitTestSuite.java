package com.library.unit.facade;

import com.library.client.CurrencyExchangeClient;
import com.library.dto.currencies.CurrencyDto;
import com.library.dto.currencies.CurrencyExchangeDto;
import com.library.service.facade.CurrencyExchangeFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeFacadeUnitTestSuite {

    @InjectMocks
    private CurrencyExchangeFacade currencyExchangeFacade;

    @Mock
    private CurrencyExchangeClient exchangeClient;

    @Test
    void testExchangeCurrency() {
        // Given
        String currencyFrom = "PLN";
        String currencyTo = "GBP";
        double amount = 24.99;
        CurrencyDto currencyDto = new CurrencyDto();
        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto(currencyDto);
        when(exchangeClient.retrieveCurrencyData(currencyFrom,currencyTo,amount)).thenReturn(currencyExchangeDto);
        // When
        String result = currencyExchangeFacade.exchangeCurrency(currencyFrom, currencyTo, amount);
        System.out.println(result);
        // Then
        assertEquals("0.0",result);
        verify(exchangeClient, times(1)).retrieveCurrencyData(currencyFrom,currencyTo,amount);
    }
}
