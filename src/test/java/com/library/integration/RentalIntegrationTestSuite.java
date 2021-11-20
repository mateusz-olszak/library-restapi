package com.library.integration;

import com.library.dao.BookRepository;
import com.library.dao.CopyRepository;
import com.library.dao.ReaderRepository;
import com.library.dao.RentalRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RentalIntegrationTestSuite {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopyRepository copyRepository;

    @Test
    void testCreateRental() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        // When
        Rental savedRental = rentalRepository.save(rental);
        // Then
        assertThat(savedRental).isNotNull();
        assertThat(savedRental.getId()).isGreaterThan(0);
        assertThat(savedRental.getCopy().getId()).isEqualTo(savedCopy.getId());
        assertThat(savedRental.getReader().getId()).isEqualTo(savedReader.getId());
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
        copyRepository.deleteById(savedCopy.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testGetRental() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        Rental savedRental = rentalRepository.save(rental);
        // When
        Rental rentalDb = rentalRepository.findById(savedRental.getId()).get();
        // Then
        assertThat(rentalDb).isNotNull();
        assertEquals(savedRental.getId(),rentalDb.getId());
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
        copyRepository.deleteById(savedCopy.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testGetRentals() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental1 = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        Rental rental2 = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        Rental rental3 = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        rentalRepository.save(rental1);
        rentalRepository.save(rental2);
        rentalRepository.save(rental3);
        // When
        List<Rental> rentals = (List<Rental>) rentalRepository.findAll();
        // Then
        assertEquals(3, rentals.size());
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
        copyRepository.deleteById(savedCopy.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testDeleteRental_bookShouldNotBeDeleted() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        Rental savedRental = rentalRepository.save(rental);
        // When
        rentalRepository.deleteById(savedRental.getId());
        // Then
        boolean rentalExists = rentalRepository.existsById(savedRental.getId());
        boolean bookExists = bookRepository.existsById(savedBook.getId());
        assertThat(rentalExists).isEqualTo(false);
        assertThat(bookExists).isEqualTo(true);
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
        copyRepository.deleteById(savedCopy.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testDeleteRental_readerShouldNotBeDeleted() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        Rental savedRental = rentalRepository.save(rental);
        // When
        rentalRepository.deleteById(savedRental.getId());
        // Then
        boolean rentalExists = rentalRepository.existsById(savedRental.getId());
        boolean readerExists = readerRepository.existsById(savedReader.getId());
        assertThat(rentalExists).isEqualTo(false);
        assertThat(readerExists).isEqualTo(true);
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
        copyRepository.deleteById(savedCopy.getId());
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testDeleteRental_copyShouldNotBeDeleted() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = new Book("BookTitle","BookDesc",1948);
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        Rental savedRental = rentalRepository.save(rental);
        // When
        rentalRepository.deleteById(savedRental.getId());
        // Then
        boolean rentalExists = rentalRepository.existsById(savedRental.getId());
        boolean copyExists = copyRepository.existsById(savedCopy.getId());
        assertThat(rentalExists).isEqualTo(false);
        assertThat(copyExists).isEqualTo(true);
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
        copyRepository.deleteById(savedCopy.getId());
        bookRepository.deleteById(savedBook.getId());
    }


}
