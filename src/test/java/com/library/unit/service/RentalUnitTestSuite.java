package com.library.unit.service;

import com.library.dao.RentalRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.dto.books.BookDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.service.RentalService;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RentalUnitTestSuite {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateRental() {
        // Given
        String password = "password";
        String encodedPass = passwordEncoder.encode(password);
        String email = "email@gmail.com";
        Reader reader = new Reader(email,encodedPass, new Date());
        Book book = Book.builder().author("Author").title("Title").yearOfPublication(1950).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        Rental rental = new Rental(copy,reader, null,null,null);
        // When
        when(rentalRepository.save(any())).thenReturn(rental);
        Rental savedRental = rentalService.createRental(rental);
        // Then
        assertThat(savedRental.getCopy().getStatus()).isNotNull();
        assertThat(savedRental.getCompleted()).isNotNull();
    }

    @Test
    void testCompleteRental() {
        // Given
        String password = "password";
        String encodedPass = passwordEncoder.encode(password);
        String email = "email@gmail.com";
        Reader reader = new Reader(email,encodedPass, new Date());
        Book book = Book.builder().author("Author").title("Title").yearOfPublication(1950).build();
        Copy copy = new Copy(book, Status.AVAILABLE);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        // When
        when(rentalRepository.save(any())).thenReturn(rental);
        Rental completedRental = rentalService.completeRental(rental);
        // Then
        assertThat(completedRental.getCompleted()).isEqualTo(Status.COMPLETED);
        assertThat(completedRental.getCopy().getStatus()).isEqualTo(Status.AVAILABLE);
    }

    @Test
    void testFindRentalWithWrongId_throwsElementNotFoundException() {
        // Given
        int wrongId = 20;
        // When Then
        when(rentalRepository.findById(anyInt())).thenThrow(new ElementNotFoundException());
        assertThrows(ElementNotFoundException.class, () -> rentalService.findRental(wrongId));
    }
}
