package com.library.controllers;

import com.library.service.facade.CurrencyExchangeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyExchangeController {

    private final CurrencyExchangeFacade currencyExchangeFacade;

    @GetMapping("/v1/exchange/currency")
    public String exchangeCurrency(
            @RequestParam("from") String base,
            @RequestParam("to") String currencyTo,
            @RequestParam("amount") double amount
    ) {
        return currencyExchangeFacade.exchangeCurrency(base, currencyTo, amount);
    }
}
