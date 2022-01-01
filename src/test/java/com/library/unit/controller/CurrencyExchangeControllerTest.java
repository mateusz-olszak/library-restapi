package com.library.unit.controller;

import com.library.controllers.CurrencyExchangeController;
import com.library.service.facade.CurrencyExchangeFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(controllers = CurrencyExchangeController.class, useDefaultFilters = false)
@AutoConfigureMockMvc
@Import(CurrencyExchangeController.class)
public class CurrencyExchangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyExchangeFacade currencyExchangeFacade;

    @WithMockUser
    @Test
    void testExchangeCurrency() throws Exception {
        // Given
        String currencyFrom = "GBP";
        String currencyTo = "PLN";
        double amount = 1;
        String result = "5.5";
        when(currencyExchangeFacade.exchangeCurrency(currencyFrom,currencyTo,amount)).thenReturn(result);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/exchange/currency?from=" + currencyFrom + "&to=" + currencyTo + "&amount=" + amount))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(String.valueOf(5.5)));
    }
}
