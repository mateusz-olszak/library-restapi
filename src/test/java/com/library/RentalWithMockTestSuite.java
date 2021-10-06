package com.library;

import com.library.dao.RentalRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.exceptions.ElementNotFoundException;
import com.library.service.RentalService;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RentalWithMockTestSuite {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void testCreateRental() throws ElementNotFoundException {
        // Given
        Reader reader = new Reader("John", "Smith", new Date());
        Book book = new Book("Harry Potter","J.K Rowling",2003);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Rental rental = new Rental(
                copy1, reader,
                LocalDate.now(), LocalDate.now().plusDays(7),
                Status.COMPLETED
        );
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalRepository.findById(rental.getId())).thenReturn(Optional.ofNullable(rental));

        // When
        rentalService.createRental(rental);
        Rental actualRental = rentalService.findRental(rental.getId());

        // Then
        assertEquals(Status.IN_USE, actualRental.getCompleted());
        assertEquals(Status.RENTED, actualRental.getCopy().getStatus());
    }

    @Test
    void testCompleteRental() throws ElementNotFoundException {
        // Given
        Reader reader = new Reader("John", "Smith", new Date());
        Book book = new Book("Harry Potter","J.K Rowling",2003);
        Copy copy1 = new Copy(book, Status.AVAILABLE);
        Rental rental = new Rental(
                copy1, reader,
                LocalDate.now(), LocalDate.now().plusDays(7),
                Status.IN_USE
        );
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalRepository.findById(rental.getId())).thenReturn(Optional.ofNullable(rental));

        // When
        rentalService.completeRental(rental);
        Rental actualRental = rentalService.findRental(rental.getId());

        // Then
        assertEquals(Status.COMPLETED, actualRental.getCompleted());
        assertEquals(Status.AVAILABLE, actualRental.getCopy().getStatus());
    }

}
