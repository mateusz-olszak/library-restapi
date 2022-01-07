package com.library.unit.controller;

import com.google.gson.Gson;
import com.library.controllers.BookController;
import com.library.dto.books.BookDto;
import com.library.dto.books.RentalDto;
import com.library.dto.googleapi.GoogleImageLinksDto;
import com.library.dto.googleapi.GoogleVolumeInfoDto;
import com.library.controllers.ModelFillerService;
import com.library.service.facade.BookFacade;
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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(controllers = BookController.class, useDefaultFilters = false)
@AutoConfigureMockMvc
@Import(BookController.class)
public class BookControllerUnitTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookFacade bookFacade;
    @MockBean
    private ModelFillerService modelFillerService;

    @WithMockUser
    @Test
    void testRetrieveAllBooks() throws Exception {
        // Given
        String keyword = null;
        int page = 1;
        GoogleImageLinksDto googleImageLinksDto = new GoogleImageLinksDto();
        GoogleVolumeInfoDto volumeInfoDto = new GoogleVolumeInfoDto();
        volumeInfoDto.setGoogleImageLinksDto(googleImageLinksDto);
        BookDto bookDto1 = new BookDto();
        bookDto1.setPhoto(null);
        bookDto1.setGoogleVolumeInfoDto(volumeInfoDto);
        BookDto bookDto2 = new BookDto();
        bookDto2.setPhoto(null);
        bookDto2.setGoogleVolumeInfoDto(volumeInfoDto);
        BookDto bookDto3 = new BookDto();
        bookDto3.setPhoto(null);
        bookDto3.setGoogleVolumeInfoDto(volumeInfoDto);
        Map<String,Object> modelMap = new HashMap<>();
        modelMap.put("books",Arrays.asList(bookDto1,bookDto2,bookDto3));
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        when(bookFacade.listBooksByPageWithKeyword(keyword,page)).thenReturn(modelMap);
        // When // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
                .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.hasSize(3)));
    }

    @WithMockUser
    @Test
    void testViewBookById() throws Exception {
        // Given
        int bookId = 1;
        GoogleImageLinksDto googleImageLinksDto = new GoogleImageLinksDto();
        GoogleVolumeInfoDto volumeInfoDto = new GoogleVolumeInfoDto();
        volumeInfoDto.setGoogleImageLinksDto(googleImageLinksDto);
        BookDto book = new BookDto();
        book.setPhoto(null);
        book.setGoogleVolumeInfoDto(volumeInfoDto);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("book", book);
        resultMap.put("copies", 2);
        resultMap.put("isDescEmpty", true);
        resultMap.put("rental", new RentalDto());
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        when(bookFacade.viewBookById(bookId)).thenReturn(resultMap);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/books/view/" + bookId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("view_book"))
                .andExpect(MockMvcResultMatchers.model().attribute("copies", 2))
                .andExpect(MockMvcResultMatchers.model().attribute("isDescEmpty", true));
    }

    @WithMockUser
    @Test
    void testGetNewBookTemplate() throws Exception {
        // Given
        Map<String, Object> resultMap = new HashMap<>();
        GoogleImageLinksDto googleImageLinksDto = new GoogleImageLinksDto();
        GoogleVolumeInfoDto volumeInfoDto = new GoogleVolumeInfoDto();
        volumeInfoDto.setGoogleImageLinksDto(googleImageLinksDto);
        BookDto book = new BookDto();
        book.setPhoto(null);
        resultMap.put("book", book);
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        when(bookFacade.printNewBookTemplate()).thenReturn(resultMap);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/books/new"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test
    void testSaveBook() throws Exception {
        // Given
        BookDto book = new BookDto();
        Gson gson = new Gson();
        String jsonContent = gson.toJson(book);
        doNothing().when(bookFacade).saveBook(book);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/books/new")
                .content(jsonContent).with(SecurityMockMvcRequestPostProcessors.csrf()).param("action","signup"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));
    }

    @WithMockUser
    @Test
    void testPrintEditBookPage() throws Exception {
        // Given
        int bookId = 1;
        Map<String, Object> resultMap = new HashMap<>();
        GoogleImageLinksDto googleImageLinksDto = new GoogleImageLinksDto();
        GoogleVolumeInfoDto volumeInfoDto = new GoogleVolumeInfoDto();
        volumeInfoDto.setGoogleImageLinksDto(googleImageLinksDto);
        BookDto book = new BookDto();
        book.setPhoto(null);
        resultMap.put("book", book);
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        when(bookFacade.printEditPage(bookId)).thenReturn(resultMap);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/books/edit/" + bookId)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).param("action","signup"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("edit_book"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("book"));
    }

    @WithMockUser
    @Test
    void testEditBook() throws Exception {
        // Given
        BookDto bookDto = new BookDto();
        Gson gson = new Gson();
        String jsonContent = gson.toJson(bookDto);
        doNothing().when(bookFacade).updateBook(bookDto);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/books/edit/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("action","signup"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));
    }

    @WithMockUser
    @Test
    void testDeleteBook() throws Exception {
        // Given
        int bookId = 1;
        doNothing().when(bookFacade).deleteBook(bookId);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/books/delete/" + bookId))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/books"));
    }
}
