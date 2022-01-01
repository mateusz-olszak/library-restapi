package com.library.unit.controller;

import com.library.controllers.ReaderController;
import com.library.controllers.ReaderRestController;
import com.library.service.facade.ReaderFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(controllers = ReaderRestController.class, useDefaultFilters = false)
@AutoConfigureMockMvc
@Import(ReaderRestController.class)
public class ReaderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReaderFacade readerFacade;

    @WithMockUser
    @Test
    void testIfEmailIsUnique() throws Exception {
        // Given
        String email = "test@gmail.com";
        String result = "OK";
        when(readerFacade.checkDuplicateEmail(email)).thenReturn(result);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/readers/register/check_email?email=" + email)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).param("action","signup"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("OK"));
    }

    @WithMockUser
    @Test
    void testDeleteReader() throws Exception {
        // Given
        int readerId = 1;
        doNothing().when(readerFacade).deleteReader(readerId);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/readers/delete/" + readerId)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).param("action","signup"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
