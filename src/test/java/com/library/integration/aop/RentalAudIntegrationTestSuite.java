package com.library.integration.aop;

import com.library.dao.*;
import com.library.domain.*;
import com.library.service.RentalService;
import com.library.status.Auditorium;
import com.library.status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RentalAudIntegrationTestSuite {

    @Autowired
    private RentalService rentalService;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private RentAudRepository rentAudRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopyRepository copyRepository;

    @BeforeEach
    public void setup() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@gmail.com");
    }

    @Test
    void testSaveRentAud() {
        // Given
        Reader reader = new Reader("test@gmail.com","user",new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental = new Rental(copy, reader);
        int savedReaderId = savedReader.getId();
        int savedBookId = savedBook.getId();
        // When
        Rental savedRental = rentalService.createRental(rental);
        // Then
        List<RentsAud> savedAuds = (List<RentsAud>) rentAudRepository.findAll();
        RentsAud savedRentAud = savedAuds.stream().findFirst().get();
        int savedRentAudId = savedRentAud.getId();
        assertEquals(1, savedAuds.size());
        assertEquals("test@gmail.com", savedRentAud.getAudOwner());
        assertEquals(Auditorium.INSERT, savedRentAud.getEventType());

        readerRepository.deleteById(savedReaderId);
        bookRepository.deleteById(savedBookId);
        rentAudRepository.deleteById(savedRentAudId);
    }

    @Test
    void testDeleteRentAud() {
        // Given
        Reader reader = new Reader("test@gmail.com","user",new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        copyRepository.save(copy);
        Rental rental = new Rental(copy, reader, LocalDate.now(), LocalDate.now().plusDays(7),Status.IN_USE);
        Rental savedRental = rentalRepository.save(rental);
        int savedReaderId = savedReader.getId();
        int savedBookId = savedBook.getId();
        int savedRentalId = savedRental.getId();
        // When
        rentalService.deleteRental(savedRentalId);
        // Then
        List<RentsAud> savedAuds = (List<RentsAud>) rentAudRepository.findAll();
        RentsAud savedRentAud = savedAuds.stream().findFirst().get();
        int deleteRentAudId = savedRentAud.getId();
        assertEquals(1, savedAuds.size());
        assertEquals("test@gmail.com", savedRentAud.getAudOwner());
        assertEquals(Auditorium.DELETE, savedRentAud.getEventType());

        readerRepository.deleteById(savedReaderId);
        bookRepository.deleteById(savedBookId);
        rentAudRepository.deleteById(deleteRentAudId);
    }

    @Test
    void testUpdateRentAud() {
        // Given
        Reader reader = new Reader("test@gmail.com","user",new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.RENTED);
        copyRepository.save(copy);
        Rental rental = new Rental(copy, reader, LocalDate.now(), LocalDate.now().plusDays(7),Status.IN_USE);
        rentalRepository.save(rental);
        int savedReaderId = savedReader.getId();
        int savedBookId = savedBook.getId();
        // When
        rentalService.completeRental(rental);
        // Then
        List<RentsAud> savedAuds = (List<RentsAud>) rentAudRepository.findAll();
        RentsAud savedRentAud = savedAuds.stream().findFirst().get();
        int deleteRentAudId = savedRentAud.getId();
        assertEquals(1, savedAuds.size());
        assertEquals("test@gmail.com", savedRentAud.getAudOwner());
        assertEquals(Auditorium.UPDATE, savedRentAud.getEventType());

        readerRepository.deleteById(savedReaderId);
        bookRepository.deleteById(savedBookId);
        rentAudRepository.deleteById(deleteRentAudId);
    }
}
