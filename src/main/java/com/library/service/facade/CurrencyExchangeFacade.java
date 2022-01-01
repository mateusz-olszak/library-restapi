package com.library.service.facade;

import com.library.client.CurrencyExchangeClient;
import com.library.dto.currencies.CurrencyExchangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyExchangeFacade {

    private final CurrencyExchangeClient currencyExchangeClient;

    public String exchangeCurrency(String base, String currencyTo, double amount) {
        CurrencyExchangeDto currencyExchangeDto = currencyExchangeClient.retrieveCurrencyData(base, currencyTo, amount);
        switch (currencyTo) {
            case "PLN":
                return String.valueOf(currencyExchangeDto.getCurrencies().getPlnD());
            case "GBP":
                return String.valueOf(currencyExchangeDto.getCurrencies().getGbp());
            case "EUR":
                return String.valueOf(currencyExchangeDto.getCurrencies().getEuro());
            case "USD":
                return String.valueOf(currencyExchangeDto.getCurrencies().getUsd());
            case "MXN":
                return String.valueOf(currencyExchangeDto.getCurrencies().getMxn());
            default:
                return String.valueOf(0);
        }
    }

}
