package com.library.unit.controller;

import com.google.gson.Gson;
import com.library.controllers.CopyController;
import com.library.dto.books.CopyDto;
import com.library.service.facade.CopyFacade;
import com.library.status.Status;
import org.hamcrest.Matchers;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(controllers = CopyController.class, useDefaultFilters = false)
@AutoConfigureMockMvc
@Import(CopyController.class)
public class CopyControllerUnitTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CopyFacade copyFacade;

    @WithMockUser
    @Test
    void testGetAllCopies() throws Exception {
        // Given
        CopyDto copy1 = new CopyDto(1, 1, Status.AVAILABLE);
        CopyDto copy2 = new CopyDto(1, 1, Status.AVAILABLE);
        CopyDto copy3 = new CopyDto(1, 1, Status.AVAILABLE);
        List<CopyDto> copies = Arrays.asList(copy1,copy2,copy3);
        when(copyFacade.getAllCopies()).thenReturn(copies);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/copies"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test
    void testGetAllCopiesForGivenBookId() throws Exception {
        // Given
        int bookId = 1;
        CopyDto copyDto1 = new CopyDto(1, 1, Status.AVAILABLE);
        CopyDto copyDto2 = new CopyDto(1, 1, Status.AVAILABLE);
        CopyDto copyDto3 = new CopyDto(1, 1, Status.AVAILABLE);
        List<CopyDto> copies = Arrays.asList(copyDto1,copyDto2,copyDto3);
        when(copyFacade.getAllCopiesForGivenBookId(bookId)).thenReturn(copies);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/copies?id=" + bookId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
    }

    @WithMockUser
    @Test
    void testGetCopy() throws Exception {
        // Given
        int bookId = 1;
        CopyDto copyDto = new CopyDto(1, 1, Status.AVAILABLE);
        when(copyFacade.getCopy(bookId)).thenReturn(copyDto);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/copies/" + bookId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("AVAILABLE")));
    }

    @WithMockUser
    @Test
    void testSaveCopy() throws Exception {
        // Given
        CopyDto copyDto = new CopyDto(1, 1, Status.AVAILABLE);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(copyDto);
        doNothing().when(copyFacade).saveCopy(copyDto);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/copies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("action","signup"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test
    void testDeleteCopy() throws Exception {
        // Given
        int copyId = 1;
        doNothing().when(copyFacade).deleteCopy(copyId);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/copies/" + copyId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("action","signup"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test
    void testChangeCopyStatus() throws Exception {
        // Given
        int copyId = 1;
        Status newStatus = Status.RENTED;
        CopyDto newDto = new CopyDto(1, 1, newStatus);
        when(copyFacade.changeCopyStatus(copyId,Status.RENTED)).thenReturn(newDto);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/copies/" + copyId + "?status=" + newStatus)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("action","signup"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
