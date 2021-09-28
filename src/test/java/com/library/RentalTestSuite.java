package com.library;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.exceptions.ElementNotFoundException;
import com.library.exceptions.NoBooksAvailableException;
import com.library.service.CopyService;
import com.library.service.RentalService;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RentalTestSuite {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CopyService copyService;

    @Test
    void testCreateRental(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE.name());
        Rental expectedRental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7));
        rentalService.createRental(expectedRental);

        Rental actualRental = null;
        int expectedId = expectedRental.getId();
        int actualId = 0;
        try {
            actualRental = rentalService.findRental(expectedId);
            actualId = actualRental.getId();
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(expectedId, actualId);

        rentalService.deleteRental(actualId);
    }

    @Test
    void testCreateRentalWithoutAvailableCopy(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.RENTED.name());
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7));

        assertThrows(NoBooksAvailableException.class, () -> {
           rentalService.createRental(rental);
        });
    }

    @Test
    void testCreateOneAvailableBookAndTryToRentForTwoReader(){
        Reader reader1 = new Reader("Anthony","Joshua",new Date());
        Reader reader2 = new Reader("John","Smith",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE.name());
        Rental rental1 = new Rental(copy,reader1, LocalDate.now(),LocalDate.now().plusDays(7));
        Rental rental2 = new Rental(copy,reader2, LocalDate.now(),LocalDate.now().plusDays(7));
        rentalService.createRental(rental1);

        assertThrows(NoBooksAvailableException.class, () -> {
            rentalService.createRental(rental2);
        });
    }

    @Test
    void testCreateTwoAvailableBookAndTryToRentForTwoReaders(){
        Reader reader1 = new Reader("Anthony","Joshua",new Date());
        Reader reader2 = new Reader("John","Smith",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE.name());
        Copy copy2 = new Copy(book, Status.AVAILABLE.name());
        book.setCopy(List.of(copy1,copy2));
        Rental rental1 = new Rental(copy1,reader1, LocalDate.now(),LocalDate.now().plusDays(7));
        Rental rental2 = new Rental(copy2,reader2, LocalDate.now(),LocalDate.now().plusDays(7));
        rentalService.createMultipleRental(List.of(rental1,rental2));

        Rental actualRental1 = null;
        Rental actualRental2 = null;
        int actualId1 = rental1.getId();
        int actualId2 = rental2.getId();
        try {
            actualRental1 = rentalService.findRental(actualId1);
            actualRental2 = rentalService.findRental(actualId2);
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(Status.RENTED.name(), actualRental1.getCopy().getStatus());
        assertEquals(Status.RENTED.name(), actualRental2.getCopy().getStatus());
    }

    @Test
    void testCreateTwoAvailableBookAndTryToRentForThreeReaders(){
        Reader reader1 = new Reader("Anthony","Joshua",new Date());
        Reader reader2 = new Reader("John","Smith",new Date());
        Reader reader3 = new Reader("Mark","Winston",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy1 = new Copy(book, Status.AVAILABLE.name());
        Copy copy2 = new Copy(book, Status.AVAILABLE.name());
        book.setCopy(List.of(copy1,copy2));
        Rental rental1 = new Rental(copy1,reader1, LocalDate.now(),LocalDate.now().plusDays(7));
        Rental rental2 = new Rental(copy2,reader2, LocalDate.now(),LocalDate.now().plusDays(7));
        Rental rental3 = new Rental(copy2,reader3, LocalDate.now(),LocalDate.now().plusDays(7));


        assertThrows(NoBooksAvailableException.class, () -> {
            rentalService.createMultipleRental(List.of(rental1,rental2,rental3));
        });
    }

    @Test
    void testCreateRental_checkIfStatusChanged(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE.name());
        Rental expectedRental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7));
        rentalService.createRental(expectedRental);

        Rental actualRental = null;
        int expectedId = expectedRental.getId();
        int actualId = 0;
        try {
            actualRental = rentalService.findRental(expectedId);
            actualId = actualRental.getId();
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(Status.RENTED.name(), actualRental.getCopy().getStatus());

        rentalService.deleteRental(actualId);
    }

    @Test
    void testReturnBook_checkIfBookStatusChanged(){
        Reader reader = new Reader("Anthony","Joshua",new Date());
        Book book = new Book("The Big Fisherman","Lloyd C. Douglas",1948);
        Copy copy = new Copy(book, Status.AVAILABLE.name());
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7));
        rentalService.createRental(rental);
        int copyId = copy.getId();

        rentalService.completeRental(rental);
        Copy expectedCopy = null;
        try {
            expectedCopy = copyService.findCopy(copyId);
        }catch (ElementNotFoundException e){
            e.getMessage();
        }

        assertEquals(Status.AVAILABLE.name(), expectedCopy.getStatus());
    }

}
