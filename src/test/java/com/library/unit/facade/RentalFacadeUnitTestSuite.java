package com.library.unit.facade;

import com.library.domain.*;
import com.library.service.BookService;
import com.library.service.CopyService;
import com.library.service.ReaderService;
import com.library.service.RentalService;
import com.library.service.facade.RentalFacade;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalFacadeUnitTestSuite {

    @InjectMocks
    private RentalFacade rentalFacade;

    @Mock
    private RentalService rentalService;
    @Mock
    private ReaderService readerService;
    @Mock
    private CopyService copyService;
    @Mock
    private BookService bookService;

    @Test
    void testGetRentalsForReader() {
        // Given
        Reader reader = new Reader();
        reader.setEmail("test@gmail.com");
        ReaderDetails readerDetails = new ReaderDetails(reader);
        when(readerService.findReaderByEmail(anyString())).thenReturn(reader);
        when(rentalService.findRentalsForReader(reader)).thenReturn(new ArrayList<>());
        // When
        rentalFacade.getRentalsForReader(readerDetails);
        // Then
        verify(readerService, times(1)).findReaderByEmail(anyString());
        verify(rentalService, times(1)).findRentalsForReader(reader);
    }

    @Test
    void testGetRentalsForAdmin() {
        // Given
        when(rentalService.findAllRentals()).thenReturn(new ArrayList<>());
        // When
        rentalFacade.getRentalsForAdmin();
        // Then
        verify(rentalService, times(1)).findAllRentals();
    }

    @Test
    void testCreateRental() {
        // Given
        int bookId = 1;
        Reader reader = new Reader();
        reader.setEmail("test@gmail.com");
        ReaderDetails readerDetails = new ReaderDetails(reader);
        Book book = Book.builder().author("Author").title("Title").yearOfPublication(1948).build();
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Copy copy2 = new Copy(book, Status.AVAILABLE);
        Copy copy3 = new Copy(book, Status.AVAILABLE);
        when(readerService.findReaderByEmail(anyString())).thenReturn(reader);
        when(copyService.retrieveAvailableCopiesForGivenId(bookId)).thenReturn(Arrays.asList(copy1,copy2,copy3));
        when(bookService.findBook(bookId)).thenReturn(book);
        when(rentalService.createRental(any())).thenReturn(new Rental());
        // When
        rentalFacade.createRental(readerDetails,bookId);
        // Then
        verify(readerService, times(1)).findReaderByEmail(anyString());
        verify(copyService, times(1)).retrieveAvailableCopiesForGivenId(bookId);
        verify(bookService, times(1)).findBook(bookId);
        verify(rentalService, times(1)).createRental(any());
    }

    @Test
    void testCompleteRental() {
        // Given
        int rentalId = 1;
        Rental rental = new Rental();
        when(rentalService.findRental(rentalId)).thenReturn(rental);
        when(rentalService.completeRental(rental)).thenReturn(rental);
        // When
        rentalFacade.completeRental(rentalId);
        // Then
        verify(rentalService, times(1)).findRental(rentalId);
        verify(rentalService, times(1)).completeRental(rental);
    }

    @Test
    void testPrintEditRentalPage() {
        // Given
        int rentalId = 1;
        when(rentalService.findRental(rentalId)).thenReturn(new Rental());
        // When
        rentalFacade.printEditRentalPage(rentalId);
        // Then
        verify(rentalService, times(1)).findRental(rentalId);
    }

    @Test
    void testSaveEdittedRental() {
        // Given
        int rentalId = 1;
        Copy copy = new Copy(new Book(), Status.AVAILABLE);
        Rental rental = new Rental();
        rental.setCopy(copy);
        rental.setId(rentalId);
        when(rentalService.findRental(rentalId)).thenReturn(rental);
        when(rentalService.saveRental(rental)).thenReturn(rental);
        // When
        rentalFacade.saveEdittedRental(rental);
        // Then
        verify(rentalService, times(1)).findRental(rentalId);
        verify(rentalService, times(1)).saveRental(rental);
    }

    @Test
    void testDeleteRental() {
        // Given
        int rentalId = 1;
        Copy copy = new Copy(new Book(), Status.AVAILABLE);
        Rental rental = new Rental();
        rental.setCopy(copy);
        when(rentalService.findRental(rentalId)).thenReturn(rental);
        doNothing().when(rentalService).deleteRental(rentalId);
        // When
        rentalFacade.deleteRental(rentalId);
        // Then
        verify(rentalService, times(1)).findRental(rentalId);
        verify(rentalService, times(1)).deleteRental(rentalId);
    }
}
