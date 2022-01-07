package com.library.unit.controller;

import com.library.controllers.ReaderController;
import com.library.dto.books.ReaderDto;
import com.library.controllers.ModelFillerService;
import com.library.service.facade.ReaderFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(controllers = ReaderController.class, useDefaultFilters = false)
@AutoConfigureMockMvc
@Import(ReaderController.class)
public class ReaderControllerUnitTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReaderFacade readerFacade;
    @MockBean
    private ModelFillerService modelFillerService;

    @WithMockUser
    @Test
    void testViewLoginForm_returnLoginTemplate() throws Exception {
        // Given
        when(readerFacade.printLoginPage(any(),any())).thenReturn("login");
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/readers/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @WithMockUser
    @Test
    void testShowRegisterForm() throws Exception {
        // Given
        Map<String, Object> resultMap = new HashMap<>();
        ReaderDto readerDto = new ReaderDto();
        resultMap.put("reader", readerDto);
        when(readerFacade.printRegisterForm()).thenReturn(resultMap);
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/readers/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @WithMockUser
    @Test
    void testRegisterReader() throws Exception {
        // Given
        String email = "test@gmail.com";
        String password = "password";
        doNothing().when(readerFacade).registerReader(email,password);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/readers/register/save?email=" + email + "&password=" + password)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).param("action","signup"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));
    }
}
