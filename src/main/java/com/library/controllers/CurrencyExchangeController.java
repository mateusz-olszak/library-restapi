package com.library.controllers;

import com.library.dto.currencies.CurrencyDto;
import com.library.dto.currencies.CurrencyExchangeDto;
import com.library.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @GetMapping("/v1/exchange/currency")
    public String exchangeCurrency(
            @RequestParam("from") String base,
            @RequestParam("to") String currencyTo,
            @RequestParam("amount") double amount
    ) {
        CurrencyExchangeDto currencyExchangeDto = currencyExchangeService.exchangeCurrency(base, currencyTo, amount);
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
