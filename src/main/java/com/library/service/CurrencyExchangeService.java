package com.library.service;

import com.library.client.CurrencyExchangeClient;
import com.library.dto.currencies.CurrencyExchangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {

    private final CurrencyExchangeClient currencyExchangeClient;

    public CurrencyExchangeDto exchangeCurrency(String base, String currencyTo, double amount) {
        return currencyExchangeClient.retrieveCurrencyData(base,currencyTo,amount);
    }
}
