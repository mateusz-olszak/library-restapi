package com.library.unit.controller;

import com.google.gson.Gson;
import com.library.controllers.RentalController;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.controllers.ModelFillerService;
import com.library.service.facade.RentalFacade;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(controllers = RentalController.class, useDefaultFilters = false)
@AutoConfigureMockMvc
@Import(RentalController.class)
public class RentalControllerUnitTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalFacade rentalFacade;
    @MockBean
    private ModelFillerService modelFillerService;

    @WithMockUser
    @Test
    void testGetRentals() throws Exception {
        // Given
        Book book = new Book();
        book.setPhoto("photo");
        Copy copy = new Copy(book, Status.AVAILABLE);
        Map<String, Object> resultMap = new HashMap<>();
        Rental rental1 = new Rental();
        rental1.setCopy(copy);
        rental1.setCompleted(Status.IN_USE);
        Rental rental2 = new Rental();
        rental2.setCopy(copy);
        rental2.setCompleted(Status.IN_USE);
        Rental rental3 = new Rental();
        rental3.setCopy(copy);
        rental3.setCompleted(Status.IN_USE);
        List<Rental> rentals = Arrays.asList(rental1,rental2,rental3);
        resultMap.put("rentals",rentals);
        when(rentalFacade.getRentalsForReader(any())).thenReturn(resultMap);
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/reader/rentals"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("my_rentals"))
               .andExpect(MockMvcResultMatchers.model().attribute("rentals", Matchers.hasSize(3)));
    }

    @WithMockUser
    @Test
    void getRentalsForAdmin() throws Exception {
        // Given
        Reader reader = new Reader();
        reader.setEmail("test@gmail.com");
        Book book = new Book();
        book.setPhoto("photo");
        Copy copy = new Copy(book, Status.AVAILABLE);
        Map<String, Object> resultMap = new HashMap<>();
        Rental rental1 = new Rental();
        rental1.setCopy(copy);
        rental1.setCompleted(Status.IN_USE);
        rental1.setReader(reader);
        Rental rental2 = new Rental();
        rental2.setCopy(copy);
        rental2.setCompleted(Status.IN_USE);
        rental2.setReader(reader);
        Rental rental3 = new Rental();
        rental3.setCopy(copy);
        rental3.setCompleted(Status.IN_USE);
        rental3.setReader(reader);
        List<Rental> rentals = Arrays.asList(rental1,rental2,rental3);
        resultMap.put("rentals",rentals);
        when(rentalFacade.getRentalsForAdmin()).thenReturn(resultMap);
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/rentals"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin_rentals"))
                .andExpect(MockMvcResultMatchers.model().attribute("rentals", Matchers.hasSize(3)));
    }

    @WithMockUser
    @Test
    void testCreateRental() throws Exception {
        // Given
        int bookId = 1;
        Map<String,Object> resultMap = new HashMap<>();
        Reader reader = new Reader();
        reader.setEmail("test@gmail.com");
        Book book = new Book();
        Copy copy = new Copy(book, Status.AVAILABLE);
        Rental rental = new Rental(copy, reader);
        resultMap.put("reader", reader);
        resultMap.put("book", book);
        resultMap.put("rental", rental);
        when(rentalFacade.createRental(any(),anyInt())).thenReturn(resultMap);
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/rentals?id=" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).param("action","signup"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/reader/rentals"));
    }

    @WithMockUser
    @Test
    void testCompleteRental() throws Exception {
        // Given
        int rentalId = 1;
        doNothing().when(rentalFacade).completeRental(rentalId);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/rentals/complete/" + rentalId))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/reader/rentals"));
    }

    @WithMockUser
    @Test
    void testPrintEditRentalPage() throws Exception {
        // Given
        int rentalId = 1;
        Rental rental = new Rental(new Copy(new Book(), Status.AVAILABLE), new Reader());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rental", rental);
        resultMap.put("available",Status.AVAILABLE);
        resultMap.put("rented",Status.RENTED);
        resultMap.put("destroyed",Status.DESTROYED);
        resultMap.put("lost",Status.LOST);
        resultMap.put("completed",Status.COMPLETED);
        resultMap.put("in_use",Status.IN_USE);
        when(rentalFacade.printEditRentalPage(rentalId)).thenReturn(resultMap);
        doCallRealMethod().when(modelFillerService).fullFillModel(any(),anyMap());
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/rentals/" + rentalId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("edit_rental"))
                .andExpect(MockMvcResultMatchers.model().attribute("rented", Status.RENTED));
    }

    @WithMockUser
    @Test
    void testSaveEdittedRental() throws Exception {
        // Given
        Rental rental = new Rental(new Copy(new Book(), Status.AVAILABLE), new Reader());
        Gson gson = new Gson();
        String jsonContent = gson.toJson(rental);
        doNothing().when(rentalFacade).saveEdittedRental(rental);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/rentals/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).param("action","signup"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/rentals"));
    }

    @WithMockUser
    @Test
    void testDeleteRental() throws Exception {
        // Given
        int rentalId = 1;
        doNothing().when(rentalFacade).deleteRental(rentalId);
        // When Then
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/rentals/delete/" + rentalId))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/rentals"));
    }
}
